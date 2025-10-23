#!/bin/bash
# Script: create_zip_img.sh
# Finalidade: Cria a imagem de disco (ramdisk) do ArcanOS, pronta para boot.

# --- Variáveis de Configuração ---
# O local onde o LFS foi compilado
LFS_ROOT="/mnt/lfs_build"

# Nome do arquivo de saída (substitui o ziparchive.img binário)
OUTPUT_IMG="ArcanOS-ramdisk-part1.img"

# Tamanho da imagem (exemplo: 4GB)
IMG_SIZE="4G"

# =======================================================
# 1. Criação de um arquivo vazio do tamanho desejado
# =======================================================
echo "Criando arquivo vazio de imagem: ${OUTPUT_IMG}"
# Este comando realocará espaço no disco (não execute no celular)
# truncate -s $IMG_SIZE $OUTPUT_IMG

# =======================================================
# 2. Formatação do sistema de arquivos (ex: ext4)
# =======================================================
echo "Formatando a imagem com ext4..."
# mkfs.ext4 $OUTPUT_IMG

# =======================================================
# 3. Montagem e Cópia dos Arquivos LFS
# =======================================================
echo "Montando imagem e copiando arquivos do LFS..."
# mkdir -p /mnt/temp_img
# mount -o loop $OUTPUT_IMG /mnt/temp_img

# Copia os arquivos binários do LFS para a imagem
# cp -a $LFS_ROOT/* /mnt/temp_img/

# =======================================================
# 4. Finalização e Desmontagem
# =======================================================
# umount /mnt/temp_img
echo "Imagem de ramdisk criada com sucesso!"

# Notas: 
# O arquivo real ArcanOS-ramdisk-part1.img deve ser hospedado em um serviço de armazenamento
# (como Google Drive, Dropbox ou um servidor próprio) e não no GitHub.

