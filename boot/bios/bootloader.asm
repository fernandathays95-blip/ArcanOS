; bootloader.asm - stage1 simples para ArcanOS (carrega kernel.bin em 0x1000:0x0000)
; Assemble: nasm -f bin bootloader.asm -o bootloader.bin

[BITS 16]
[ORG 0x7E00]

start:
    cli
    xor ax, ax
    mov ds, ax
    mov es, ax

    mov si, msg
    call print_string

    ; carregar kernel (assume kernel começando no setor 3 e ocupa N setores)
    mov bx, 0x0000
    mov es, 0x1000        ; carregar em 0x1000:0x0000
    mov di, 0x0000
    mov cx, 8             ; número de setores do kernel (ajuste conforme build)
    mov dx, [drive]
    mov si, 3             ; setor inicial do kernel
.load_loop:
    push cx
    push si
    mov ah, 0x02
    mov al, 1
    mov ch, 0
    mov cl, si
    mov dh, 0
    int 0x13
    jc disk_fail
    add si, 1
    pop si
    pop cx
    loop .load_loop

    ; saltar para kernel em 0x1000:0x0000
    jmp 0x1000:0x0000

print_string:
    mov ah, 0x0E
.nextch:
    lodsb
    or al, al
    jz .ret2
    int 0x10
    jmp .nextch
.ret2:
    ret

disk_fail:
    mov si, err
    call print_string
    hlt

msg db "ArcanOS stage1 loaded. Jumping to kernel...", 0
err db "KERNEL LOAD FAILED", 0
drive dw 0

times 1024-($-$$) db 0
