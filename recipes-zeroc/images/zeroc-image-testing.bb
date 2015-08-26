DESCRIPTION = "Everything required to run the ZeroC Ice test suite"

IMAGE_FEATURES += " \
    debug-tweaks \
    ssh-server-dropbear \
    "

IMAGE_INSTALL = " \
    packagegroup-core-boot \
    ${CORE_IMAGE_EXTRA_INSTALL} \
    glibc-gconvs \
    glibc-utils \
    python-misc \
    python-modules \
    rsync \
    zeroc-ice \
    "

IMAGE_FSTYPES = "tar.bz2 ext3 sdcard"

inherit core-image

ENABLE_BINARY_LOCALE_GENERATION = "1"
IMAGE_LINGUAS = "en-us"
GLIBC_GENERATE_LOCALES = "en_US.UTF-8"

IMAGE_ROOTFS_EXTRA_SPACE_append = "+ 2000000"
