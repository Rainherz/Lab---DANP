package com.example.core.network.api

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class MockInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url
        val keyword = url.queryParameter("keywordSearch")
        
        val json = if (keyword.isNullOrBlank()) {
            """
            {
                "vulnerabilities": [
                    {
                        "cve": {
                            "id": "CVE-2026-0001",
                            "published": "2026-05-10T12:00:00.000",
                            "descriptions": [
                                {
                                    "lang": "en",
                                    "value": "Remote Code Execution in Spring Framework core through deserialization. Impacting Java applications."
                                }
                            ],
                            "metrics": {
                                "cvssMetricV31": [
                                    {
                                        "cvssData": {
                                            "baseScore": 9.8,
                                            "baseSeverity": "CRITICAL"
                                        }
                                    }
                                ]
                            }
                        }
                    },
                    {
                        "cve": {
                            "id": "CVE-2026-0002",
                            "published": "2026-05-15T10:30:00.000",
                            "descriptions": [
                                {
                                    "lang": "en",
                                    "value": "SQL Injection in auth controller via user-agent header in Express/NodeJS backend."
                                }
                            ],
                            "metrics": {
                                "cvssMetricV31": [
                                    {
                                        "cvssData": {
                                            "baseScore": 8.8,
                                            "baseSeverity": "HIGH"
                                        }
                                    }
                                ]
                            }
                        }
                    },
                    {
                        "cve": {
                            "id": "CVE-2026-0003",
                            "published": "2026-05-20T14:15:00.000",
                            "descriptions": [
                                {
                                    "lang": "en",
                                    "value": "Cross-Site Scripting (XSS) in comment section component of React frontend library."
                                }
                            ],
                            "metrics": {
                                "cvssMetricV31": [
                                    {
                                        "cvssData": {
                                            "baseScore": 6.1,
                                            "baseSeverity": "MEDIUM"
                                        }
                                    }
                                ]
                            }
                        }
                    },
                    {
                        "cve": {
                            "id": "CVE-2026-0004",
                            "published": "2026-05-22T09:00:00.000",
                            "descriptions": [
                                {
                                    "lang": "en",
                                    "value": "Information exposure through error logs on profile page in Django/Python application."
                                }
                            ],
                            "metrics": {
                                "cvssMetricV31": [
                                    {
                                        "cvssData": {
                                            "baseScore": 3.3,
                                            "baseSeverity": "LOW"
                                        }
                                    }
                                ]
                            }
                        }
                    },
                    {
                        "cve": {
                            "id": "CVE-2026-0005",
                            "published": "2026-05-25T16:45:00.000",
                            "descriptions": [
                                {
                                    "lang": "en",
                                    "value": "Buffer Overflow in Bluetooth driver for Embedded systems compiled with C++ compiler."
                                }
                            ],
                            "metrics": {
                                "cvssMetricV31": [
                                    {
                                        "cvssData": {
                                            "baseScore": 9.6,
                                            "baseSeverity": "CRITICAL"
                                        }
                                    }
                                ]
                            }
                        }
                    },
                    {
                        "cve": {
                            "id": "CVE-2026-0006",
                            "published": "2026-05-28T11:20:00.000",
                            "descriptions": [
                                {
                                    "lang": "en",
                                    "value": "Denial of Service in gateway route handler written in Go using Fiber framework."
                                }
                            ],
                            "metrics": {
                                "cvssMetricV31": [
                                    {
                                        "cvssData": {
                                            "baseScore": 7.5,
                                            "baseSeverity": "HIGH"
                                        }
                                    }
                                ]
                            }
                        }
                    }
                ]
            }
            """.trimIndent()
        } else {
            if (keyword.contains("1.0.0") || keyword.lowercase().contains("safe") || keyword.lowercase().contains("segura")) {
                """
                {
                    "vulnerabilities": []
                }
                """.trimIndent()
            } else {
                """
                {
                    "vulnerabilities": [
                        {
                            "cve": {
                                "id": "CVE-2026-9091",
                                "published": "2026-05-30T10:00:00.000",
                                "descriptions": [
                                    {
                                        "lang": "es",
                                        "value": "Vulnerabilidad crítica de ejecución remota de código (RCE) detectada en $keyword. Permite la inyección de comandos no autorizados."
                                    }
                                ],
                                "metrics": {
                                    "cvssMetricV31": [
                                        {
                                            "cvssData": {
                                                "baseScore": 9.8,
                                                "baseSeverity": "CRITICAL"
                                            }
                                        }
                                    ]
                                },
                                "references": [
                                    {
                                        "url": "https://github.com/advisories/GHSA-example-patch-critical",
                                        "tags": ["Patch", "Third Party Advisory"]
                                    }
                                ]
                            }
                        },
                        {
                            "cve": {
                                "id": "CVE-2026-9092",
                                "published": "2026-05-30T10:15:00.000",
                                "descriptions": [
                                    {
                                        "lang": "es",
                                        "value": "Fuga de información confidencial en el módulo de autenticación de $keyword debido a un error de validación de tokens de sesión."
                                    }
                                ],
                                "metrics": {
                                    "cvssMetricV31": [
                                        {
                                            "cvssData": {
                                                "baseScore": 7.5,
                                                "baseSeverity": "HIGH"
                                            }
                                        }
                                    ]
                                },
                                "references": [
                                    {
                                        "url": "https://nvd.nist.gov/vuln/detail/CVE-2026-9092",
                                        "tags": ["Vendor Advisory"]
                                    }
                                ]
                            }
                        }
                    ]
                }
                """.trimIndent()
            }
        }
        
        return Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_2)
            .code(200)
            .message("OK")
            .body(json.toResponseBody("application/json".toMediaType()))
            .build()
    }
}
