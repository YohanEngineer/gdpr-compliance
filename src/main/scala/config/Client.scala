package config

import java.sql.Date

case class Client(
                   IdentifiantClient: Long,
                   Nom: String,
                   Prenom: String,
                   Adresse: String,
                   DateDeSouscription: Date)
