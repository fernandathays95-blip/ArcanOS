#ifndef ARCANOS_ZIPARCHIVE_H
#define ARCANOS_ZIPARCHIVE_H

#include <string>
#include <vector>

// Define a estrutura para as partes do arquivo (part1, part2, etc.)
struct ArchivePart {
    std::string filename;
    long long size_bytes;
    // Pode incluir um checksum para verificação de integridade (muito importante!)
    std::string checksum_sha256; 
};

// Classe principal para gerenciar o desempacotamento na RAM
class ZipArchiveLoader {
public:
    // Construtor: Inicializa com a lista de partes do sistema operacional.
    ZipArchiveLoader(const std::vector<ArchivePart>& parts);

    // Método principal: Baixa e descompacta o sistema na RAM (ramdisk).
    // Retorna true em sucesso, false em falha.
    bool load_system_to_ram();

private:
    std::vector<ArchivePart> archive_parts;

    // Métodos privados auxiliares
    bool fetch_part(const ArchivePart& part);
    bool decompress_to_ram(const std::string& data);
    bool verify_integrity(const std::string& data, const std::string& expected_checksum);
};

#endif // ARCANOS_ZIPARCHIVE_H
