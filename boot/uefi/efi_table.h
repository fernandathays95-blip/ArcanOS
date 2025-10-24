/* efi_table.h - definições mínimas EFI para compilar um app simples
   Este header é propositalmente reduzido; para builds reais use EDK2 headers. */

#ifndef EFI_TABLE_H
#define EFI_TABLE_H

#include <stdint.h>

typedef uint64_t EFI_STATUS;
typedef void* EFI_HANDLE;
typedef uint16_t CHAR16;

#define EFI_SUCCESS 0

/* Simple console out proto */
typedef struct {
    EFI_STATUS (*OutputString)(void *This, const CHAR16 *String);
} SIMPLE_TEXT_OUTPUT_PROTOCOL;

typedef struct {
    SIMPLE_TEXT_OUTPUT_PROTOCOL *ConOut;
} EFI_SYSTEM_TABLE;

#endif /* EFI_TABLE_H */
