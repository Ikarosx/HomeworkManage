package cn.ikarosx.homework.controller;

import cn.ikarosx.homework.entity.FileSystem;
import cn.ikarosx.homework.exception.ResponseResult;
import cn.ikarosx.homework.util.SessionUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Ikarosx
 * @date 2020/9/15 9:37
 */
@Api(tags = "FastDfs接口")
@RestController
@RequestMapping("/file")
public class FdfsController {
  @Autowired private RestTemplate restTemplate;

  @Value("${fdfsUrl}")
  private String fdfsUrl;

  @PostMapping("/upload")
  @ApiOperation("上传文件")
  public ResponseResult uploadFile(MultipartFile file) {
    FileSystem fileSystem = new FileSystem();
    fileSystem.setBusinessKey("homework");
    fileSystem.setUserId(SessionUtils.getId());
    MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
    params.add("file", file);
    params.add("fileSystem", fileSystem);
    ResponseEntity<ResponseResult> entity =
        restTemplate.postForEntity(fdfsUrl + "/fileSystem", file, ResponseResult.class);
    ResponseResult postResult = entity.getBody();
    return postResult;
  }

  @PostMapping("/delete")
  @ApiOperation("删除文件")
  public ResponseResult deleteFile(String fileSystemId) {

    ResponseEntity<ResponseResult> entity =
        restTemplate.exchange(
            fdfsUrl + "/fileSystem/" + fileSystemId, HttpMethod.DELETE, null, ResponseResult.class);
    ResponseResult deleteResult = entity.getBody();
    return deleteResult;
  }
}
