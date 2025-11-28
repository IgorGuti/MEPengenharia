package com.mep.app // ✅ Pacote corrigido

import android.os.Bundle
import android.util.Log // Importação necessária para usar o Log.d
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.mep.app.ui.repository.UserViewModel
import com.google.android.material.navigation.NavigationView
import com.mep.app.ui.fragment.AboutFragment
import com.mep.app.databinding.ActivityMainBinding
import com.google.firebase.messaging.FirebaseMessaging // Importação necessária para FCM

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding // ✅ Correção do tipo
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 🔑 CHAMADA PARA OBTER O TOKEN FCM NA INICIALIZAÇÃO
        getFCMToken()

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_monitorEletrico,
                R.id.nav_monitorReservatorio
            ),
            drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_about -> {
                val aboutFragment = AboutFragment()

                supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment_content_main, aboutFragment)
                    .addToBackStack(null)
                    .commit()

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    // 🔑 FUNÇÃO PARA OBTER O TOKEN FCM
    private fun getFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM_TOKEN", "Falha ao buscar o token de registro FCM", task.exception)
                return@addOnCompleteListener
            }

            // Pega o token de registro
            val token = task.result

            // Exibe o token no Logcat (Filtre por "FCM_TOKEN" para copiar)
            Log.d("FCM_TOKEN", "Token de Registro FCM: $token")

            // TODO: Aqui, você enviaria o token para o seu servidor para uso futuro.
            // sendTokenToBackend(token)
        }
    }

    companion object {
    }
}