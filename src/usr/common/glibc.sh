#!/bin/bash
# Script: glibc.sh
# Finalidade: Compila e instala a GNU C Library (Glibc).
# Local: src/usr/common/

# Versão da Glibc (escolhida para estabilidade/compatibilidade)
GLIBC_VERSION="2.38"
SOURCE_DIR="glibc-${GLIBC_VERSION}"
BUILD_DIR="glibc-build"

# Assume-se que o ambiente LFS (variáveis LFS, LFS_TGT) já está configurado.
# Este script deve ser executado APÓS a Toolchain inicial ser criada.

# 1. Download e Extração (Simulado)
# wget -c "https://ftp.gnu.org/gnu/glibc/${SOURCE_DIR}.tar.xz"
# tar -xf "${SOURCE_DIR}.tar.xz"

# 2. Configuração
mkdir -v "${BUILD_DIR}"
cd "${BUILD_DIR}"

# O parâmetro --host é crucial para o Cross-Compiling (ARM64 vs x86_64)
# O configure detectará automaticamente a arquitetura se o TARGET for definido.
# O TARGET_TGT é definido no script de setup de arquitetura (ex: src/x86_64/server/build.sh)

../${SOURCE_DIR}/configure \
    --prefix=/usr \
    --host="${LFS_TGT}" \
    --build="$(../${SOURCE_DIR}/scripts/config.guess)" \
    --enable-kernel=4.19 \
    --enable-stack-protector=strong \
    --with-headers=/usr/include \
    --disable-werror \
    libc_cv_slibdir=/lib

# 3. Compilação
# make

# 4. Instalação
# make install

# 5. Configurações Finais (Locale e Timezone)
# Localização (Importante para Unicode no Menu Mode!)
# /usr/sbin/localedef -i POSIX -f UTF-8 C.UTF-8

# Limpeza
cd ../
rm -rf "${BUILD_DIR}"
# rm -rf "${SOURCE_DIR}"
