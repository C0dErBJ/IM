package com.AlphaZ.controller;

import com.AlphaZ.constant.StatusCode;
import com.AlphaZ.dao.FileDAO;
import com.AlphaZ.entity.FileEntity;
import com.AlphaZ.entity.api.ResponseModel;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * ProjectName: IM
 * PackageName: com.AlphaZ.controller
 * User: C0dEr
 * Date: 2017/5/26
 * Time: 下午2:42
 * Description:This is a class of com.AlphaZ.controller
 */
@RequestMapping("file")
@RestController
@Transactional
public class FileController {

    @Resource
    FileDAO fileDAO;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseModel upload(@RequestParam MultipartFile file_data) {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFilename(file_data.getOriginalFilename());
        try {
            fileEntity.setFile(file_data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseModel("新增失败", StatusCode.FAIL, null);
        }
        fileDAO.save(fileEntity);
        return new ResponseModel("新增成功", StatusCode.SUCCESS, fileEntity);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public void getPic(@PathVariable Long id, HttpServletResponse response) {
        List<FileEntity> file = this.fileDAO.findByField(FileEntity.class, "id", id);
        if (file.size() == 0 || file.size() > 1) {
            return;
        }
        response.setContentType("image/png");
        try {
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            outputStream.write(file.get(0).getFile());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
