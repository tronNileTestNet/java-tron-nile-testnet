package org.tron.core.zen;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.PostConstruct;

import org.tron.common.crypto.ckzg4844.CKZG4844JNI;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KZGPointEvaluationInitService {

  @PostConstruct
  private void init() {
    initCKZG4844();
  }

  public static void freeSetup() {
    CKZG4844JNI.freeTrustedSetup();
  }

  public static void initCKZG4844() {
    logger.info("init ckzg 4844 begin");

    CKZG4844JNI.loadNativeLibrary();

    String setupFile = getSetupFile("trusted_setup.txt");

    CKZG4844JNI.loadTrustedSetup(setupFile, 0);

    logger.info("init ckzg 4844 done");
  }

  private static String getSetupFile(String fileName) {
    InputStream in = Thread.currentThread().getContextClassLoader()
        .getResourceAsStream("kzg-trusted-setups" + File.separator + fileName);
    File fileOut = new File(System.getProperty("java.io.tmpdir")
        + File.separator + fileName + "." + System.currentTimeMillis());
    try {
      FileUtils.copyToFile(in, fileOut);
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
    }
    if (fileOut.exists()) {
      fileOut.deleteOnExit();
    }
    return fileOut.getAbsolutePath();
  }
}