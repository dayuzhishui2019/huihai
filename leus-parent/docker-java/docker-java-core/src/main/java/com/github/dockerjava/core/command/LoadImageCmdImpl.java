package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.LoadImageCmd;
import com.github.dockerjava.api.model.LoadResponseItem;

import javax.annotation.Nonnull;
import java.io.InputStream;

import static com.google.common.base.Preconditions.checkNotNull;

public class LoadImageCmdImpl extends AbstrDockerCmd<LoadImageCmd, LoadResponseItem> implements LoadImageCmd {

    private InputStream imageStream;

    /**
     * @param imageStream the InputStream of the tar file
     */
    public LoadImageCmdImpl(LoadImageCmd.Exec exec, InputStream imageStream) {
        super(exec);
        withImageStream(imageStream);
    }

    @Override
    public InputStream getImageStream() {
        return imageStream;
    }

    /**
     * @param imageStream the InputStream of the tar file
     */
    @Override
    public LoadImageCmdImpl withImageStream(@Nonnull InputStream imageStream) {
        checkNotNull(imageStream, "imageStream was not specified");
        this.imageStream = imageStream;
        return this;
    }
}
