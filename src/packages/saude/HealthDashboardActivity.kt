// src/packages/saude/HealthDashboardActivity.kt
// Gerenciamento da UI e dos dados de saude no ArcanOS.

package arcanos.packages.saude

// Importacoes simuladas (do ArcanOS Framework)
import arcanos.app.Activity
import arcanos.os.Bundle
import arcanos.hardware.sensor.HealthSensorManager
import arcanos.hardware.sensor.SensorDataListener
import arcanos.util.Log
import arcanos.ui.TextView
import arcanos.ui.Switch

/**
 * Atividade que exibe o painel de saude e gerencia a conexao com sensores.
 */
class HealthDashboardActivity : Activity(), SensorDataListener {

    // Gerenciador de Sensores do Kernel
    private lateinit var sensorManager: HealthSensorManager
    
    // Elementos da UI
    private lateinit var tvHeartRate: TextView
    private lateinit var tvStepsCount: TextView
    private lateinit var switchSync: Switch

    /**
     * Inicializacao da Atividade.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.health_dashboard)
        
        // Inicializar Views (simulado)
        tvHeartRate = findViewById(R.id.tv_heart_rate)
        tvStepsCount = findViewById(R.id.tv_steps_count)
        switchSync = findViewById(R.id.switch_wearable_sync)
        
        // Inicializar o gerenciador de sensores (simulado: JNI/Binder)
        sensorManager = getSystemService(HealthSensorManager::class.java)

        // Tentar registrar para receber atualizacoes de dados.
        if (sensorManager.checkPermissions()) {
            sensorManager.registerListener(this)
            switchSync.isChecked = sensorManager.isSyncEnabled()
        } else {
            // Solicita permissoes se nao tiver
            Log.w("SAUDE", "Permissoes de saude pendentes. Solicitando ao usuario.")
            sensorManager.requestPermissions() 
        }

        switchSync.setOnCheckedChangeListener { _, isChecked ->
            sensorManager.setSyncEnabled(isChecked)
            Log.i("SAUDE", "Sincronizacao de wearable: $isChecked")
        }
    }
    
    /**
     * Implementacao da interface SensorDataListener.
     * Chamado em tempo real com novos dados.
     */
    override fun onDataUpdate(dataType: String, value: Int) {
        when (dataType) {
            "heart_rate" -> {
                runOnUiThread {
                    tvHeartRate.text = "$value bpm"
                    // Logica de alerta de Hard Brick (DKEJZ) nao seria aqui, mas sim no kernel
                    if (value > 150) { 
                        Log.e("SAUDE_RISCO", "Frequencia cardiaca elevada: $value")
                    }
                }
            }
            "steps_count" -> {
                runOnUiThread {
                    tvStepsCount.text = value.toString()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }
    
    // Simula a obtencao de um servico do sistema
    private fun getSystemService(clazz: Class<HealthSensorManager>): HealthSensorManager {
        // Implementacao real chamaria o framework do OS
        return HealthSensorManager.getInstance()
    }
}

// SIMULAÇÕES DE CLASSES DE FRAMEWORK (para que o código Kotlin seja válido)
class R {
    class layout {
        companion object {
            const val health_dashboard = 1
        }
    }
    class id {
        companion object {
            const val tv_heart_rate = 1
            const val tv_steps_count = 2
            const val switch_wearable_sync = 3
            const val btn_manage_permissions = 4
        }
    }
}
class HealthSensorManager private constructor() {
    companion object {
        fun getInstance() = HealthSensorManager()
    }
    fun checkPermissions() = true // Assume que a permissao e concedida
    fun requestPermissions() {}
    fun registerListener(listener: SensorDataListener) {}
    fun unregisterListener(listener: SensorDataListener) {}
    fun isSyncEnabled() = true
    fun setSyncEnabled(enabled: Boolean) {}
}
