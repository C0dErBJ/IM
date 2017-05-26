package com.AlphaZ.util.file.importer;

import com.AlphaZ.util.file.itf.IImporter;
import com.AlphaZ.util.io.FileUtil;
import com.AlphaZ.util.valid.ValideHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ProjectName: AlphaZ
 * PackageName: com.AlphaZ.com.AlphaZ.util.file.importer
 * User: C0dEr
 * Date: 2017/3/23
 * Time: 上午11:43
 * Description:This is a class of com.AlphaZ.com.AlphaZ.util.file.importer
 */
public class BaseImporter<T> implements IImporter {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private String FilePath;
    protected String EntityName;
    private List<String> SourceData;
    protected List<List<String>> HandledData;
    public String org;

    public BaseImporter(String entityName, String path, String org) {
        FilePath = path + "/" + entityName;
        EntityName = entityName;
        HandledData = new ArrayList<>();
        this.org = org;
    }


    public void excute() {
        log.error("文件名为[{}],正在读取数据....", EntityName);
        if (!readFile(true)) {
            return;
        }
        log.error("文件名为[{}],读取数据成功....", EntityName);
        log.error("文件名为[{}],正在切割数据....", EntityName);
        if (!convertData()) {
            return;
        }
        log.error("文件名为[{}],正在数据切割成功....", EntityName);
        log.error("文件名为[{}],正在解析数据....", EntityName);
        processData();
        log.error("文件名为[{}],数据解析成功....", EntityName);
    }

    @Override
    public boolean convertData() {
        for (String line : SourceData) {
            List<String> str = Arrays.asList(line.split("\\|"));
            HandledData.add(str);
        }
        if (ValideHelper.isNullOrEmpty(HandledData)) {
            log.error("文件名为[{}]数据切割失败...", EntityName);
            return false;
        }
        return true;
    }


    @Override
    public boolean readFile(boolean isCheckContainer) {
        SourceData = FileUtil.readFileByline(FilePath);
        if (ValideHelper.isNullOrEmpty(SourceData)) {
            log.info("文件名为[{}]数据为空...", EntityName);
            return false;
        }
        if (isCheckContainer) {
            if (!SourceData.get(0).trim().equals("BEGIN") || !SourceData.get(SourceData.size()).equals("END")) {
                log.error("文件名为[{}]数据不完整...", EntityName);
                return false;
            } else {
                SourceData = SourceData.subList(1, SourceData.size() - 1);
            }
        }
        return true;
    }


    public void processData() {

    }


}
