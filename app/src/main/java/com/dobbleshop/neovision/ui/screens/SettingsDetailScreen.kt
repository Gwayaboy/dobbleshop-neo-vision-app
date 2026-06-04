package com.dobbleshop.neovision.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsDetailScreen(
    itemId: String,
    onBackClick: () -> Unit
) {
    val spec = detailSpec(itemId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(spec.title) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = spec.subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            spec.cards.forEach { card ->
                SettingsDetailCard(title = card.title, description = card.description)
            }

            if (spec.showToggles) {
                FeatureSwitchesCard()
            }

            if (spec.showPrimaryAction) {
                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(spec.primaryActionLabel)
                }
            }
        }
    }
}

@Composable
private fun SettingsDetailCard(
    title: String,
    description: String
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
            )
        }
    }
}

@Composable
private fun FeatureSwitchesCard() {
    var enabledA by remember { mutableStateOf(true) }
    var enabledB by remember { mutableStateOf(false) }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ToggleRow(
                title = "Option principale",
                checked = enabledA,
                onCheckedChange = { enabledA = it }
            )

            Divider()

            ToggleRow(
                title = "Option avancée",
                checked = enabledB,
                onCheckedChange = { enabledB = it }
            )

            Spacer(modifier = Modifier.height(4.dp))

            OutlinedButton(onClick = { }, modifier = Modifier.fillMaxWidth()) {
                Text("Enregistrer")
            }
        }
    }
}

@Composable
private fun ToggleRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = MaterialTheme.typography.bodyLarge)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

private data class DetailCard(
    val title: String,
    val description: String
)

private data class DetailSpec(
    val title: String,
    val subtitle: String,
    val cards: List<DetailCard>,
    val showToggles: Boolean = false,
    val showPrimaryAction: Boolean = true,
    val primaryActionLabel: String = "Appliquer"
)

private fun detailSpec(itemId: String): DetailSpec {
    return when (itemId) {
        "onboarding" -> DetailSpec(
            title = "Onboarding",
            subtitle = "Guide de démarrage pas à pas.",
            cards = listOf(
                DetailCard("Assistant d'installation", "Connexion du feeder, calibration capteurs et validation réseau."),
                DetailCard("Checklist maison", "Positionnement, sécurité électrique et test de distribution.")
            ),
            showPrimaryAction = true,
            primaryActionLabel = "Relancer l'onboarding"
        )

        "water_management" -> DetailSpec(
            title = "Gestion de l'eau",
            subtitle = "Suivi de consommation, alarmes de niveau et hygiène.",
            cards = listOf(
                DetailCard("Objectif hydratation", "Cible quotidienne par animal et seuil d'alerte personnalisable."),
                DetailCard("Nettoyage", "Rappel automatique de nettoyage du réservoir et de la pompe.")
            ),
            showToggles = true,
            primaryActionLabel = "Sauvegarder"
        )

        "communication" -> DetailSpec(
            title = "Communication appareil",
            subtitle = "Configuration Bluetooth et Wi-Fi du feeder.",
            cards = listOf(
                DetailCard("Bluetooth", "Scan et association de l'appareil de proximité."),
                DetailCard("Wi-Fi", "Choix SSID, force du signal et tests de latence.")
            ),
            showToggles = true
        )

        "notifications" -> DetailSpec(
            title = "Notifications",
            subtitle = "Préférences d'alertes et priorités.",
            cards = listOf(
                DetailCard("Canaux", "Push mobile, email résumé quotidien et alertes urgentes."),
                DetailCard("Événements", "Bourrage, niveau bas eau/croquettes, sécurité et batterie.")
            ),
            showToggles = true
        )

        "multi_user" -> DetailSpec(
            title = "Multi-utilisateurs",
            subtitle = "Partage sécurisé en version V1.5.",
            cards = listOf(
                DetailCard("Invitations", "Ajout d'utilisateurs avec rôles Lecteur ou Gestionnaire."),
                DetailCard("Audit", "Journal de modifications et accès récents.")
            ),
            showPrimaryAction = true,
            primaryActionLabel = "Créer une invitation"
        )

        "hardware_diagnostics" -> DetailSpec(
            title = "Diagnostics matériels",
            subtitle = "État des composants ESP32-S3, RPi Zero 2W et capteurs.",
            cards = listOf(
                DetailCard("CPU / Mémoire", "Mesure en temps réel et dernière erreur critique."),
                DetailCard("Capteurs", "HX711, VL53L0X et mmWave: calibration et statut."),
                DetailCard("I/O", "GPIO moteurs, pompe, et connectique série UART.")
            )
        )

        "reservoirs_bowl" -> DetailSpec(
            title = "Réservoirs & Gamelle",
            subtitle = "Niveaux, consommation et précision de pesée.",
            cards = listOf(
                DetailCard("Croquettes", "Niveau estimé, masse restante et alerte de recharge."),
                DetailCard("Eau", "Volume actuel, pompage journalier et dérive détectée."),
                DetailCard("Gamelle", "Poids actuel HX711 et correction d'offset.")
            )
        )

        "home_security" -> DetailSpec(
            title = "Sécurité maison",
            subtitle = "Modes mmWave et événements de présence.",
            cards = listOf(
                DetailCard("Modes", "Off, Home, Absent et Auto avec règles personnalisées."),
                DetailCard("Captures", "Preuves snapshot sur détection et historique horodaté.")
            ),
            showToggles = true
        )

        "full_history" -> DetailSpec(
            title = "Historique complet",
            subtitle = "Distributions et graphiques de tendance.",
            cards = listOf(
                DetailCard("Repas", "Journal par heure, portion prévue vs réelle."),
                DetailCard("Eau", "Volumes distribués par plage horaire et anomalies."),
                DetailCard("Sécurité", "Événements mmWave et captures associées.")
            ),
            primaryActionLabel = "Exporter CSV"
        )

        "smart_ration" -> DetailSpec(
            title = "Ration intelligente",
            subtitle = "Recommandation nutritionnelle RER/DER.",
            cards = listOf(
                DetailCard("Profil animal", "Espèce, âge, poids, niveau d'activité et objectifs."),
                DetailCard("Calcul", "Suggestion dynamique de ration et alertes de dépassement.")
            ),
            showToggles = true,
            primaryActionLabel = "Recalculer"
        )

        "firmware_ota", "firmware_esp", "firmware_rpi" -> DetailSpec(
            title = "Mise à jour firmware OTA",
            subtitle = "Gestion des versions, validation et rollback.",
            cards = listOf(
                DetailCard("Canal", "Stable / Beta selon votre environnement."),
                DetailCard("Sécurité", "Signature package, checksum et reprise après échec."),
                DetailCard("Planification", "Installation immédiate ou fenêtre nocturne.")
            ),
            primaryActionLabel = "Vérifier les mises à jour"
        )

        "user_account" -> DetailSpec(
            title = "Compte utilisateur",
            subtitle = "Profil, permissions et sécurité.",
            cards = listOf(
                DetailCard("Identité", "Nom, email et photo de profil."),
                DetailCard("Sécurité", "Mot de passe et double authentification."),
                DetailCard("Rôles", "Gestion des droits sur alimentation, caméra et réglages.")
            )
        )

        "synchronization" -> DetailSpec(
            title = "Synchronisation",
            subtitle = "État cloud et sauvegarde locale.",
            cards = listOf(
                DetailCard("Dernière synchro", "Conflits détectés et résolution automatique."),
                DetailCard("Sauvegardes", "Historique versions et restauration rapide.")
            ),
            showToggles = true,
            primaryActionLabel = "Forcer une synchronisation"
        )

        "language_region" -> DetailSpec(
            title = "Langue & Région",
            subtitle = "Langue d'affichage, formats date/heure et unités.",
            cards = listOf(
                DetailCard("Langue", "Français, English et autres locales supportées."),
                DetailCard("Région", "Unités g/ml, fuseau horaire et calendrier.")
            ),
            showToggles = false,
            primaryActionLabel = "Appliquer la langue"
        )

        "support_help" -> DetailSpec(
            title = "Support & Aide",
            subtitle = "Documentation, FAQ et contact support.",
            cards = listOf(
                DetailCard("FAQ", "Guides rapides pour alimentation, eau, caméra et sécurité."),
                DetailCard("Contact", "email: contact@dobbleshop.fr · support prioritaire."),
                DetailCard("Diagnostic partagé", "Envoyer journaux techniques au support.")
            ),
            showPrimaryAction = true,
            primaryActionLabel = "Contacter le support"
        )

        else -> DetailSpec(
            title = "Paramètre",
            subtitle = "Configuration détaillée.",
            cards = listOf(
                DetailCard("État", "Ce paramètre est prêt pour configuration." )
            )
        )
    }
}
