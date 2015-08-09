import java.io.File
import java.lang.Math._
import java.lang.System._

import com.googlecode.totallylazy.Bytes._
import com.googlecode.totallylazy.Files.write
import sbt._

import scala.reflect.io.Directory

object ResourceGenerators {
  def copy(from: File, to: File): Seq[File] =
    for {
      (f, t) <- from ** "*" pair rebase(from, to)
    } yield {
      Sync.copy(f, t)
      t
    }

  def generate(managedBase: File, base: File, target: File): Seq[sbt.File] = {
    val sourceVersion = Option(getProperty("source.version")).getOrElse("dev.build")

    val versionProperties = write(bytes(s"source=$sourceVersion"), new sbt.File(target, "versions.properties"))

    Seq(versionProperties)
  }
}