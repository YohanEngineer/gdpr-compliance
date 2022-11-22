package config

case class Config(
                   delete: Long = 1.toLong,
                   hash: Long = 1.toLong,
                   init:Boolean = false
                 )
