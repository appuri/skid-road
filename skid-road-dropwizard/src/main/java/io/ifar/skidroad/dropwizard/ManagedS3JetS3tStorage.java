package io.ifar.skidroad.dropwizard;

import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.lifecycle.Managed;
import io.ifar.skidroad.dropwizard.config.RequestLogUploadConfiguration;
import io.ifar.skidroad.dropwizard.config.SkidRoadConfiguration;
import io.ifar.skidroad.jets3t.S3JetS3tStorage;
import io.ifar.skidroad.upload.JetS3tUploadByDirectoryWorkerFactory;
import io.ifar.skidroad.upload.JetS3tUploadWorkerFactory;
import io.ifar.skidroad.upload.UploadWorkerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class ManagedS3JetS3tStorage extends S3JetS3tStorage implements Managed {
    public ManagedS3JetS3tStorage(String accessKeyID, String secretAccessKey) {
        this(accessKeyID, secretAccessKey, null);
    }

    public ManagedS3JetS3tStorage(String accessKeyID, String secretAccessKey, Map<String,String> propertyOverrides) {
        super(accessKeyID, secretAccessKey, propertyOverrides);
    }

    public static ManagedS3JetS3tStorage buildStorage(RequestLogUploadConfiguration uploadConfiguration, Environment environment) {
        return buildStorage(uploadConfiguration, environment, null);
    }

    public static ManagedS3JetS3tStorage buildStorage(RequestLogUploadConfiguration uploadConfiguration, Environment environment, Map<String,String> propertyOverrides) {
        ManagedS3JetS3tStorage storage = new ManagedS3JetS3tStorage(
                uploadConfiguration.getAccessKeyID(),
                uploadConfiguration.getSecretAccessKey(),
                propertyOverrides
        );
        environment.manage(storage);
        environment.addHealthCheck(storage.healthCheck());
        return storage;
    }

    public static UploadWorkerFactory buildWorkerFactory(S3JetS3tStorage storage, RequestLogUploadConfiguration uploadConfiguration) throws URISyntaxException {
        return new JetS3tUploadWorkerFactory(
                storage,
                new URI(uploadConfiguration.getUploadPath())
        );
    }

    public static UploadWorkerFactory buildByDirectoryWorkerFactory(S3JetS3tStorage storage, RequestLogUploadConfiguration uploadConfiguration) throws URISyntaxException {
        return new JetS3tUploadByDirectoryWorkerFactory(
                storage,
                new URI(uploadConfiguration.getUploadPath())
        );
    }

    public static UploadWorkerFactory buildWorkerFactory(RequestLogUploadConfiguration uploadConfiguration, Environment environment) throws URISyntaxException {
        return buildWorkerFactory(buildStorage(uploadConfiguration, environment), uploadConfiguration);
    }

    public static UploadWorkerFactory buildWorkerFactory(RequestLogUploadConfiguration uploadConfiguration, Environment environment, Map<String,String> propertyOverrides) throws URISyntaxException {
        return buildWorkerFactory(buildStorage(uploadConfiguration, environment, propertyOverrides), uploadConfiguration);
    }

    public static UploadWorkerFactory buildWorkerFactory(SkidRoadConfiguration skidRoadConfiguration, Environment environment) throws URISyntaxException {
        return buildWorkerFactory(skidRoadConfiguration.getRequestLogUploadConfiguration(), environment);
    }

    public static UploadWorkerFactory buildWorkerFactory(SkidRoadConfiguration skidRoadConfiguration, Environment environment,Map<String,String> propertyOverrides) throws URISyntaxException {
        return buildWorkerFactory(skidRoadConfiguration.getRequestLogUploadConfiguration(), environment, propertyOverrides);
    }


    public static UploadWorkerFactory buildByDirectoryWorkerFactory(RequestLogUploadConfiguration uploadConfiguration, Environment environment) throws URISyntaxException {
        return buildByDirectoryWorkerFactory(buildStorage(uploadConfiguration, environment), uploadConfiguration);
    }

    public static UploadWorkerFactory buildByDirectoryWorkerFactory(RequestLogUploadConfiguration uploadConfiguration, Environment environment, Map<String,String> propertyOverrides) throws URISyntaxException {
        return buildByDirectoryWorkerFactory(buildStorage(uploadConfiguration, environment, propertyOverrides), uploadConfiguration);
    }

    public static UploadWorkerFactory buildByDirectoryWorkerFactory(SkidRoadConfiguration skidRoadConfiguration, Environment environment) throws URISyntaxException {
        return buildByDirectoryWorkerFactory(skidRoadConfiguration.getRequestLogUploadConfiguration(), environment);
    }

    public static UploadWorkerFactory buildByDirectoryWorkerFactory(SkidRoadConfiguration skidRoadConfiguration, Environment environment,Map<String,String> propertyOverrides) throws URISyntaxException {
        return buildByDirectoryWorkerFactory(skidRoadConfiguration.getRequestLogUploadConfiguration(), environment, propertyOverrides);
    }
}
