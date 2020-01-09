package com.github.dockerjava.api.command;

import com.github.dockerjava.api.model.LoadResponseItem;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.io.InputStream;

public interface LoadImageCmd extends SyncDockerCmd<LoadResponseItem> {

    @CheckForNull
    InputStream getImageStream();

    /**
     * @param imageStream the InputStream of the tar file
     */
    LoadImageCmd withImageStream(@Nonnull InputStream imageStream);

    interface Exec extends DockerCmdSyncExec<LoadImageCmd, LoadResponseItem> {
    }
}
