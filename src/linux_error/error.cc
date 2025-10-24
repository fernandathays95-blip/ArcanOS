// src/linux_error/error.cc
// Lógica para exibir uma imagem de erro fatal no ambiente Linux/Userland.

#include <iostream>
#include <string>
#include <cstdlib> // Para abort()

// Define o caminho da imagem de erro dentro do mesmo diretório.
#define ERROR_IMAGE_PATH "./error_image.png"

// Simulação de uma biblioteca de UI leve (ex: Mini-GTK ou SDL)
namespace MiniUI {
    // Representação de uma imagem carregada
    struct Image {
        int width;
        int height;
        // ... dados de pixel
    };

    // Simula a função de carregamento de imagem (ex: usando libpng ou stb_image)
    Image* load_image(const std::string& path) {
        if (path.empty()) return nullptr;
        
        // Simulação: Verifica se o arquivo existe e retorna um objeto Image.
        std::cout << "[MiniUI] Tentando carregar imagem de erro: " << path << std::endl;
        
        // No mundo real, isso seria uma chamada de I/O de disco.
        // Se o sistema estiver em estado de erro, o I/O pode falhar.
        
        if (path.find("error_image") != std::string::npos) {
            Image* img = new Image();
            img->width = 1920;
            img->height = 1080;
            std::cout << "[MiniUI] Imagem carregada com sucesso. \n";
            return img;
        }
        return nullptr;
    }
    
    // Simula a função de desenho em tela cheia
    void draw_fullscreen(const Image* img) {
        if (!img) return;
        std::cout << "========================================================\n";
        std::cout << "      [ MiniUI: DESENHANDO TELA DE ERRO FATAL ]       \n";
        std::cout << "      (Imagem: " << img->width << "x" << img->height << " pixels)      \n";
        std::cout << "========================================================\n";
        
        // Aqui, o código real faria uma chamada para a API gráfica (Wayland/X11) 
        // para tomar posse da tela e exibir os pixels.
    }
}


/**
 * @brief Exibe a tela de erro fatal do Userland.
 * * Tenta carregar uma imagem de erro pré-renderizada para um display rápido.
 * @param error_message Mensagem de diagnóstico.
 */
extern "C" void show_linux_error_screen(const char* error_message) {
    std::cerr << "--- ARCANOS USERLAND ERROR ---" << std::endl;
    std::cerr << "Motivo: " << (error_message ? error_message : "Erro desconhecido.") << std::endl;
    
    MiniUI::Image* error_image = MiniUI::load_image(ERROR_IMAGE_PATH);
    
    if (error_image) {
        MiniUI::draw_fullscreen(error_image);
        delete error_image;
    } else {
        // Fallback: Se a imagem não puder ser carregada, exibe texto simples
        std::cerr << "ERRO: Falha ao carregar imagem. Exibindo modo texto.\n";
    }

    // Após exibir a mensagem, o processo Userland deve ser encerrado ou entrar em loop.
    std::cerr << "O sistema será encerrado agora. Por favor, reinicie." << std::endl;
    // abort(); // Força o encerramento do processo em Userland.
}
