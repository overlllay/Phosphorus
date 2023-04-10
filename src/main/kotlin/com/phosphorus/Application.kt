package com.phosphorus

import com.ulisesbocchio.jasyptspringbootstarter.JasyptSpringBootAutoConfiguration
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * @author wuxin
 */
@SpringBootApplication
@ImportAutoConfiguration(JasyptSpringBootAutoConfiguration::class)
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
