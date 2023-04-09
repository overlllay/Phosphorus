package com.phosphorus.config

import com.theokanning.openai.OpenAiApi
import com.theokanning.openai.service.OpenAiService
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import java.time.Duration

/**
 * @author wuxin
 */
@Configuration
@EnableConfigurationProperties(OpenAiProperties::class)
class OpenAiConfig(private val openAiProperties: OpenAiProperties) {

    @Bean
    fun openAiService(): OpenAiService {
        val openAiApi = buildOpenAiApi(openAiProperties)
        return OpenAiService(openAiApi)
    }

    companion object {
        private fun buildOpenAiApi(properties: OpenAiProperties): OpenAiApi {
            val token: String = properties.token
            val readTimeout: Duration = properties.readTimeout
            val httpClient = OpenAiService.defaultClient(token, readTimeout)
            val mapper = OpenAiService.defaultObjectMapper()
            val converterFactory: Converter.Factory = JacksonConverterFactory.create(mapper)
            return Retrofit.Builder()
                .baseUrl(properties.baseUrl)
                .client(httpClient)
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(OpenAiApi::class.java)
        }
    }
}
