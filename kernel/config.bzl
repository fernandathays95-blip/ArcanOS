# kernel/config.bzl
# Arquivo Starlark (Bazel) para carregar e expor configuracoes do kernel.

# Funcao de simulacao para carregar TOML (Bazel nao tem suporte nativo para TOML
# mas assumimos que ha uma regra externa para isso: load_toml)
# load("@bazel_tools//tools/build_defs/toml:toml.bzl", "load_toml")

# Simulamos o carregamento do TOML para criar um dicionario de configuracao.
def _load_kernel_config():
    """Simula o carregamento e parse do config.toml."""
    return {
        "ARCH": "aarch64",
        "OS_VERSION": "v0.9-alpha",
        "OPTIMIZATION_FLAGS": ["-O2", "-ffunction-sections", "-fdata-sections"],
        "LINK_FLAGS": ["-Wl,--gc-sections"],
        "ENABLE_VIRTUAL_MEMORY": True,
        "ENABLED_DRIVERS": ["nvme", "sdmmc", "ethernet_mac", "wifi_80211"],
        "MEM_BASE_ADDR": "0x80000000",
    }

# Expor o dicionario de configuracao como uma constante
KERNEL_CONFIG = _load_kernel_config()

# Funcao Starlark para gerar flags de compilacao baseadas no config.toml
def arcanos_kernel_cflags(config):
    """Retorna a lista de CFLAGS necessarias para o build do kernel."""
    cflags = []
    
    # Adiciona as flags de otimizacao
    cflags.extend(config["OPTIMIZATION_FLAGS"])
    
    # Adiciona definicoes de pre-processador com base nas features
    if config["ENABLE_VIRTUAL_MEMORY"]:
        cflags.append("-DCONFIG_VIRT_MEM_ON")

    if "nvme" in config["ENABLED_DRIVERS"]:
        cflags.append("-DCONFIG_DRIVER_NVME")

    cflags.append("-DARC_ARCH=\"%s\"" % config["ARCH"])
    
    return cflags

# Macro que sera usada nos arquivos BUILD para construir o kernel
def arcanos_kernel_library(name, srcs, deps = []):
    """
    Define uma cc_library para o kernel ArcanOS com configuracoes padrao.
    """
    
    # Carregar configuracoes globais
    config = KERNEL_CONFIG

    # Obter CFLAGS e LDFLAGS especificas do kernel
    c_flags = arcanos_kernel_cflags(config)
    
    # Expor a macro como uma regra Bazel (usando o nativo cc_library)
    native.cc_library(
        name = name,
        srcs = srcs,
        deps = deps,
        copts = c_flags,  # flags de compilacao
        linkopts = config["LINK_FLAGS"], # flags de linkagem
        include_prefix = "kernel",
        strip_include_prefix = "kernel",
        visibility = ["//visibility:public"],
    )
