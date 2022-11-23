package config

case class Config(
                   delete: Long = 1.toLong,
                   deleteM : Seq[Long] = Seq(),
                   hash: Long = 1.toLong,
                   hashM : Seq[Long] = Seq(),
                   init:Boolean = false
                 )
