package com.AlphaZ.util.file;


import com.AlphaZ.util.file.itf.IImporter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ProjectName: AlphaZ
 * PackageName: com.AlphaZ.com.AlphaZ.util.file
 * User: C0dEr
 * Date: 2017/3/23
 * Time: 下午1:14
 * Description:This is a class of com.AlphaZ.com.AlphaZ.util.file
 */
public class DataImporter {
    private List<IImporter> importers = new ArrayList<>();
    private final String FINALCIAL = "LCCPXX";//理财前缀
    private final String FUND = "ZXJJCPXX";//基金前缀
    private final String QB = "ZGQBCPXX";//钱包前缀
    private final String path = "";//文件夹路径


    public void excute(String org) {

        importers.forEach(a -> a.excute());
    }

    private String generate(String name, String org) {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMDD");
        return name + "." + org + "." + sdf.format(new Date());
    }

}
