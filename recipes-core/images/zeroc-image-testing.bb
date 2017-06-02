DESCRIPTION = "Everything required to run the ZeroC Ice test suite"

IMAGE_FEATURES += "debug-tweaks ssh-server-dropbear"

DISTRO_FEATURES += " bluetooth bluez5 nfs"

IMAGE_INSTALL = " \
    os-release \
    packagegroup-core-boot \
    ${CORE_IMAGE_EXTRA_INSTALL} \
    glibc-gconvs \
    glibc-utils \
    ca-certificates \
    python-misc \
    python-modules \
    python \
    openssl \
    bzip2 \
    lmdb \
    dbus-glib \
    rsync \
    nfs-utils-client \
    git \
    "

inherit core-image
