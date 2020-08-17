package com.integrationservice.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * util class to deal with file operations
 */
@Component
@Slf4j
public class FileUtil {

    /**
     * takes file and convert it to ByteArrayResources
     *
     * @param file
     * @return {@link ByteArrayResource}
     * @throws IOException
     */
    public ByteArrayResource getContentAsResource(MultipartFile file) throws IOException {
        log.info("Entered getContentAsResource(MultipartFile) to convert file to ByteArrayResources");
        return new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };
    }
}
