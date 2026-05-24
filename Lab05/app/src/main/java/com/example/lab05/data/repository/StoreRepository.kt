package com.example.lab05.data.repository

import com.example.lab05.model.Product

class StoreRepository {
    private val products = listOf(
        Product(
            id = 1,
            name = "Laptop Gamer Nitro",
            description = "Potente laptop para gaming de última generación con gráficos trazados de rayos y procesador de alta gama.",
            price = 2499.99,
            category = "Computadoras",
            rating = 4.8f,
            reviewsCount = 45,
            stock = 5,
            features = listOf("NVIDIA RTX 4070 8GB GDDR6", "AMD Ryzen 9 7900HS (8 núcleos)", "16GB RAM DDR5 Dual Channel", "1TB SSD NVMe PCIe 4.0", "Pantalla 16\" QHD (2560x1600) 165Hz IPS"),
            imageType = "laptop"
        ),
        Product(
            id = 2,
            name = "Teclado Mecánico Apex",
            description = "Teclado mecánico premium con interruptores táctiles silenciosos, construcción en aluminio e iluminación RGB configurable.",
            price = 129.99,
            category = "Accesorios",
            rating = 4.6f,
            reviewsCount = 112,
            stock = 15,
            features = listOf("Switches Outemu Brown Silenciosos", "Iluminación RGB Dinámica por Tecla", "Teclas de PBT Double-shot Resistentes", "Cable USB-C Removible Trenzado", "Estructura de Aluminio Aeroespacial Anodizado"),
            imageType = "keyboard"
        ),
        Product(
            id = 3,
            name = "Mouse Gamer Venom",
            description = "Mouse ergonómico inalámbrico ultra-ligero diseñado para eSports de nivel competitivo con sensor óptico de alta precisión.",
            price = 79.99,
            category = "Accesorios",
            rating = 4.5f,
            reviewsCount = 89,
            stock = 25,
            features = listOf("Sensor Óptico de 20,000 DPI", "Peso Ultra-ligero de sólo 59g", "Conectividad Inalámbrica 2.4Ghz y Bluetooth", "Hasta 80 Horas de Batería Continua", "Interruptores Ópticos de Larga Duración"),
            imageType = "mouse"
        ),
        Product(
            id = 4,
            name = "Audífonos Inalámbricos Sonic",
            description = "Auriculares gamer inalámbricos con sonido envolvente 7.1 espacial, aislamiento pasivo de ruido y micrófono con cancelación de ruido.",
            price = 159.99,
            category = "Audio",
            rating = 4.7f,
            reviewsCount = 64,
            stock = 8,
            features = listOf("Sonido Espacial Virtual 7.1", "Altavoces de Neodimio de 50mm", "Conexión Inalámbrica 2.4GHz sin Latencia", "Micrófono Retráctil Reductor de Ruido", "Almohadillas de Espuma con Memoria Viscoelástica"),
            imageType = "audio"
        ),
        Product(
            id = 5,
            name = "Monitor Gamer Spectrum",
            description = "Monitor curvo ultrapanorámico de 34 pulgadas con alta tasa de refresco para una inmersión visual total en tus juegos y trabajo.",
            price = 449.99,
            category = "Computadoras",
            rating = 4.4f,
            reviewsCount = 37,
            stock = 3,
            features = listOf("Pantalla Ultrawide 21:9 Curva 1500R", "Resolución UWQHD (3440 x 1440)", "Tasa de Refresco Fluida de 165Hz", "Tiempo de Respuesta de 1ms (MPRT)", "Soporte HDR10 y AMD FreeSync Premium"),
            imageType = "monitor"
        ),
        Product(
            id = 6,
            name = "Silla Ergonómica Razer",
            description = "Silla ergonómica de alto rendimiento con soporte lumbar integrado, diseñada para mantener una postura saludable durante largas jornadas.",
            price = 299.99,
            category = "Muebles",
            rating = 4.3f,
            reviewsCount = 52,
            stock = 6,
            features = listOf("Soporte Lumbar Integrado Totalmente Ajustable", "Apoyabrazos 4D Multidireccionales", "Piel Sintética Premium de Doble Textura", "Mecanismo de Inclinación Reforzado hasta 139°", "Pistón de Gas Clase 4 de Alta Seguridad"),
            imageType = "chair"
        )
    )

    fun getProducts(): List<Product> = products

    fun getProductById(id: Int): Product? = products.find { it.id == id }

    fun getCategories(): List<String> = products.map { it.category }.distinct()

    fun getProductCountForCategory(category: String): Int = products.count { it.category == category }
}
