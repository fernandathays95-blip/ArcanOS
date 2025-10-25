//
// Implementação da Interface Mach/XNU para ArcanOS.
// Arquivo: src/XNU/XnuMachInterface.cc
//

#include "XnuMachInterface.h"
#include <sstream>

namespace arcanos::xnu::mach {

mach_task_t XnuMachInterface::createTask(const std::string& process_name) {
    mach_task_t new_task = next_task_id_++;
    active_tasks_[new_task] = process_name;
    
    std::cout << "[XNU] Task " << new_task << " ('" << process_name << "') criada." << std::endl;
    return new_task;
}

mach_port_t XnuMachInterface::allocatePort(mach_task_t task_id) {
    if (active_tasks_.find(task_id) == active_tasks_.end()) {
        std::cerr << "[XNU] Erro: Tentativa de alocar porta para Task " << task_id << " inexistente." << std::endl;
        return 0;
    }
    
    mach_port_t new_port = next_port_id_++;
    port_to_task_map_[new_port] = task_id;
    
    std::cout << "[XNU] Porta " << new_port << " alocada para Task " << task_id << "." << std::endl;
    return new_port;
}

MachResult XnuMachInterface::sendMessage(mach_port_t destination_port, const std::string& message) {
    if (port_to_task_map_.find(destination_port) == port_to_task_map_.end()) {
        std::cerr << "[XNU] Erro: Falha no envio. Porta " << destination_port << " não encontrada." << std::endl;
        return MachResult::PORT_NOT_FOUND;
    }
    
    mach_task_t target_task = port_to_task_map_[destination_port];
    std::string task_name = active_tasks_[target_task];

    std::cout << "[XNU] Mensagem Mach ('" << message << "') enviada para a Porta " 
              << destination_port << " (Task: " << task_name << ")." << std::endl;

    // Em um kernel real, o scheduler seria notificado aqui.
    return MachResult::SUCCESS;
}

MachResult XnuMachInterface::terminateTask(mach_task_t task_id) {
    if (active_tasks_.erase(task_id) == 0) {
        std::cerr << "[XNU] Erro: Tentativa de encerrar Task " << task_id << " inexistente." << std::endl;
        return MachResult::INVALID_ARGUMENT;
    }
    
    // Remove todas as portas associadas a esta tarefa
    for (auto it = port_to_task_map_.begin(); it != port_to_task_map_.end(); ) {
        if (it->second == task_id) {
            std::cout << "[XNU] Porta " << it->first << " liberada." << std::endl;
            it = port_to_task_map_.erase(it);
        } else {
            ++it;
        }
    }
    
    std::cout << "[XNU] Task " << task_id << " encerrada com sucesso." << std::endl;
    return MachResult::SUCCESS;
}

void XnuMachInterface::printPortRegistry() const {
    std::cout << "\n--- XNU Mach Port Registry ---" << std::endl;
    if (port_to_task_map_.empty()) {
        std::cout << "Nenhuma porta ativa." << std::endl;
        return;
    }
    for (const auto& pair : port_to_task_map_) {
        mach_port_t port = pair.first;
        mach_task_t task = pair.second;
        std::string task_name = active_tasks_.count(task) ? active_tasks_.at(task) : "UNKNOWN_TASK";
        std::cout << "Porta ID: " << port << " -> Task ID: " << task << " (" << task_name << ")" << std::endl;
    }
    std::cout << "------------------------------" << std::endl;
}

} // namespace arcanos::xnu::mach
