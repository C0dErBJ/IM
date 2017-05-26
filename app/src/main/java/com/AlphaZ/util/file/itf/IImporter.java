package com.AlphaZ.util.file.itf;

/**
 * ProjectName: AlphaZ
 * PackageName: com.AlphaZ.com.AlphaZ.util.file.itf
 * User: C0dEr
 * Date: 2017/3/23
 * Time: 上午11:46
 * Description:This is a class of com.AlphaZ.com.AlphaZ.util.file.itf
 */
public interface IImporter {

    void excute();

    boolean convertData();

    boolean readFile(boolean isCheckContainer);

}