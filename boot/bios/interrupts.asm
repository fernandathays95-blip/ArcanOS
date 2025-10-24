; interrupts.asm - vetor de interrupções mínimo (apenas template)
; Não substitui o vector table do BIOS real; usado apenas para organização do projeto.

; Este arquivo é um placeholder: em sistemas reais, vetores são fornecidos pelo firmware.
; Aqui mantemos handlers simples para debug (imprime e retorna).

[BITS 16]
SECTION .text

int_handler_default:
    pusha
    ; imprimir número de interrupção (em AL)
    popa
    iret
