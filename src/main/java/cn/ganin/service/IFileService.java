package cn.ganin.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Author agamgn
 * @Date 2018-08-07
 **/
public interface IFileService {
    String upload(MultipartFile file, String path);
}
