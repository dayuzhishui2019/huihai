package com.github.dockerjava.core.exec;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.command.LoadImageCmd;
import com.github.dockerjava.api.model.LoadResponseItem;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.WebTarget;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoadImageCmdExec extends AbstrSyncDockerCmdExec<LoadImageCmd, LoadResponseItem> implements
        LoadImageCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoadImageCmdExec.class);

    public LoadImageCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected LoadResponseItem execute(LoadImageCmd command) {
        WebTarget webResource = getBaseResource().path("/images/load");
        LOGGER.trace("POST: {}", webResource);
        LoadResponseItem item = webResource.request().post(command.getImageStream(), new TypeReference<LoadResponseItem>() {
        });

        Preconditions.checkState(item != null && !Strings.isNullOrEmpty(item.getStream()));
        String text = item.getStream().trim();
        Matcher matcher = pattern.matcher(text);
        if (matcher.matches()) {
            item.setRepository(matcher.group("repository"));
            item.setTag(matcher.group("tag"));
        }
        item.setImageName(text.replaceAll("Loaded image: ", "").trim());
        return item;

    }


    private static final Pattern pattern = Pattern.compile("Loaded image: (?<repository>[0-9a-zA-Z\\.\\-_]+(:\\d{1,5})?\\S+):(?<tag>[0-9a-zA-Z\\.\\-_]+)");
}
