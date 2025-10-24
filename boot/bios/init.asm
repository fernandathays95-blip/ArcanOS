; init.asm - ArcanOS BIOS boot sector
; Assembled with: nasm -f bin init.asm -o init.bin

[BITS 16]
[ORG 0x7C00]

start:
    cli
    xor ax, ax
    mov ds, ax
    mov es, ax
    mov ss, ax
    mov sp, 0x7C00

    ; mostrar mensagem
    mov si, boot_msg
    call print_string

    ; carregar setor 2 (primeiro setor do stage1) para 0x0000:0x7E00
    mov bx, 0x7E00
    mov dh, 0    ; head
    mov dl, [BOOT_DRIVE] ; drive passado pelo BIOS (será atualizado pelo loader)
    mov ch, 0
    mov cl, 2    ; setor 2 (assumindo imagem simples)
    call read_sector

    ; saltar para stage1 em 0000:7E00
    jmp 0x0000:0x7E00

; -------------------------
; rotinas utilitárias
; -------------------------
print_string:
    mov ah, 0x0E
.next_char:
    lodsb
    or al, al
    jz .ret
    int 0x10
    jmp .next_char
.ret:
    ret

; read_sector: AH = 0x02 already set inside
; inputs: DL = drive, CH = cyl, DH=head, CL=sector, ES:BX = buffer
read_sector:
    pusha
    mov ah, 0x02
    mov al, 1        ; ler 1 setor
    int 0x13
    jc disk_error
    popa
    ret
disk_error:
    mov si, disk_err
    call print_string
    hlt

; dados
boot_msg db "ArcanOS BIOS Bootloader started...", 0
disk_err db "DISK READ ERROR", 0

; espaço reservado para BIOS drive (substituir por 0x00/0x80 no momento do build se desejar)
BOOT_DRIVE db 0

times 510-($-$$) db 0
dw 0xAA55
