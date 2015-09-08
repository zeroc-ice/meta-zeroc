DESCRIPTION = "Everything required to run the ZeroC Ice test suite"

IMAGE_FEATURES += "debug-tweaks ssh-server-dropbear"

IMAGE_INSTALL = " \
    packagegroup-core-boot \
    ${CORE_IMAGE_EXTRA_INSTALL} \
    glibc-gconvs \
    glibc-utils \
    ca-certificates \
    python-misc \
    python-modules \
    rsync \
    zeroc-ice \
    zeroc-ice-python \
    zeroc-ice-utils \
    zeroc-glacier2 \
    zeroc-icebox \
    "

TOOLCHAIN_TARGET_TASK += "zeroc-ice-dev zeroc-ice-staticdev"
TOOLCHAIN_HOST_TASK  += "nativesdk-zeroc-ice-dev"

inherit core-image
