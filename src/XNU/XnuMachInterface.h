#ifndef ARCANOS_XNU_MACH_INTERFACE_H
#define ARCANOS_XNU_MACH_INTERFACE_H

#include <string>
#include <map>
#include <iostream>

namespace arcanos::xnu::mach {

// Tipos de dados fundamentais do Mach Kernel (simulados)
using mach_port_t = unsigned int;
using mach_task_t = unsigned int;

// Códigos de Retorno XNU/Mach (simulados)
enum class MachResult {
    SUCCESS,
    INVALID_ARGUMENT,
    PORT_NOT_FOUND,
    NO_MEMORY
};

/**
 * @brief Gerencia tarefas (processos) e portas de comunicação no subsistema Mach.
 * Simula a interface XNU para IPC e controle de tarefas.
 */
class XnuMachInterface {
public:
    // Cria uma nova "tarefa" (processo)
    mach_task_t createTask(const std::string& process_name);
    
    // Aloca uma nova porta de comunicação para uma tarefa
    mach_port_t allocatePort(mach_task_t task_id);
    
    // Envia uma mensagem para uma porta Mach (Comunicação Interprocessos)
    MachResult sendMessage(mach_port_t destination_port, const std::string& message);
    
    // Encerra uma tarefa específica
    MachResult terminateTask(mach_task_t task_id);

    // Mapeamento de portas para tarefas (simulação de registro)
    void printPortRegistry() const;

private:
    std::map<mach_task_t, std::string> active_tasks_;
    std::map<mach_port_t, mach_task_t> port_to_task_map_;
    mach_task_t next_task_id_ = 100;
    mach_port_t next_port_id_ = 200;

};

} // namespace arcanos::xnu::mach

#endif // ARCANOS_XNU_MACH_INTERFACE_H
