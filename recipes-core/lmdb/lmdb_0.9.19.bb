SUMMARY  = "Symas Lightning Memory-mapped Database"
DESCRIPTION = "Symas LMDB is an extraordinarily fast, memory-efficient database"
HOMEPAGE = "https://github.com/zeroc-ice/lmdb"
SECTION  = "libs"

LICENSE  = "OLDAP-2.8"
LIC_FILES_CHKSUM = "file://LICENSE;md5=153d07ef052c4a37a8fac23bc6031972"

PV = "0.9.19"
PR = "r0"

S = "${WORKDIR}/lmdb-LMDB_${PV}/libraries/liblmdb"
B = "${WORKDIR}/lmdb-LMDB_${PV}/libraries/liblmdb"

SRC_URI = "https://github.com/zeroc-ice/lmdb/archive/LMDB_${PV}.tar.gz"
SRC_URI[md5sum] = "3fedea4a16676b7585b025349b3201c4"
SRC_URI[sha256sum] = "108532fb94c6f227558d45be3f3347b52539f0f58290a7bb31ec06c462d05326"

ALLOW_EMPTY_${PN} = "1"
EXTRA_OEMAKE = "'CC=${CC}' 'AR=${AR}' 'prefix=${prefix}' 'XCFLAGS=-fPIC'"

do_compile () {
    oe_runmake
}

do_install () {
    oe_runmake install DESTDIR=${D} PREFIX=${prefix}
    rm -rf ${D}${libdir}/*.so
}

# Add native and nativesdk support
BBCLASSEXTEND += "native nativesdk"
