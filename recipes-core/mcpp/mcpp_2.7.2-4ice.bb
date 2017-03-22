SUMMARY  = "MCPP"
DESCRIPTION = "A C/C++ preprocessor"
HOMEPAGE = "https://github.com/zeroc-ice/mcpp"
SECTION  = "libs"

LICENSE  = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5ca370b75ec890321888a00cea9bc1d5"

PV = "2.7.2-4ice"
PR = "r0"

SRC_URI = "https://github.com/zeroc-ice/mcpp/archive/v${PV}.tar.gz"
SRC_URI[md5sum] = "4d6aa0862a50db25b8dff0d1582e6943"
SRC_URI[sha256sum] = "21de8c1b6f1a10822f979813d7311b9c529ee989a479cc71b4f5e48dc57ddb6a"

ALLOW_EMPTY_${PN} = "1"

do_compile () {
    oe_runmake
}

do_install () {
    oe_runmake install PREFIX=${D}/${prefix}
    for header in *.h; do
        install -Dm644 $header ${D}/${prefix}/include/$header
    done
}

# Add native and nativesdk support
BBCLASSEXTEND += "native nativesdk"
