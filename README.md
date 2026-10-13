# Liga de Fútbol — Spring Boot + MongoDB

Proyecto con **dos controladores por cada repositorio**: uno **web** (páginas HTML con Thymeleaf) y uno **API REST** (JSON).

## Estructura

```
com.ligafutbol
├── LigaFutbolApplication.java
├── controller
│   ├── api   → ClubApiController, EntrenadorApiController, JugadorApiController,
│   │           AsociacionApiController, CompeticionApiController   (@RestController, /api/...)
│   └── web   → ClubWebController, EntrenadorWebController, JugadorWebController,
│               AsociacionWebController, CompeticionWebController, HomeWebController   (@Controller, HTML)
├── service   → un servicio por entidad (ClubService, EntrenadorService, ...) + DataSeeder
├── repository
└── model
```

Ambos controladores de cada entidad usan el mismo servicio, así que las reglas de negocio
(integridad referencial, cascadas, conflictos 409) son idénticas en la web y en la API.

## Rutas

| Entidad | API REST | Web |
|---|---|---|
| Club | `/api/clubes` | `/clubes` |
| Entrenador | `/api/entrenadores` | `/entrenadores` |
| Jugador | `/api/jugadores` | `/jugadores` |
| Asociación | `/api/asociaciones` | `/asociaciones` |
| Competición | `/api/competiciones` | `/competiciones` |

### API REST (JSON)
Todas: `GET /`, `GET /{id}`, `POST /`, `PUT /{id}`, `DELETE /{id}`. Además:
- `GET /api/asociaciones/{id}/clubes` y `GET /api/competiciones/{id}/clubes` (lado inverso de las relaciones).
- Club: `PUT|DELETE /api/clubes/{id}/entrenador[/{entrenadorId}]`, `POST|DELETE /api/clubes/{id}/jugadores/{jugadorId}`,
  `PUT /api/clubes/{id}/asociacion/{asociacionId}`, `POST|DELETE /api/clubes/{id}/competiciones/{competicionId}`.

### Web (HTML)
Todas: listar (`GET /x`), formulario nuevo (`GET /x/nuevo`), crear (`POST /x`), editar (`GET /x/{id}/editar`),
actualizar (`POST /x/{id}`) y eliminar (`POST /x/{id}/eliminar`). Además:
- Club, Asociación y Competición tienen página de detalle (`GET /x/{id}`).
- En el detalle del club se gestionan las relaciones: entrenador, plantilla, asociación y competiciones.

Otras páginas: `/` (panel principal, usa la API) y `/listar` (resumen).

El tema visual (verde cancha, escudos, camisetas con dorsal, plantilla sobre la cancha y marcador) está en `src/main/resources/static/css/liga.css`; las paletas se cambian en las variables `:root`.

## Requisitos
- Java 17
- Maven
- MongoDB local o una URI de MongoDB Atlas

## Configurar la base de datos
Por seguridad no se incluye ninguna URI con credenciales. Por defecto se conecta a `mongodb://localhost:27017/liga`.
Para Atlas, define la variable de entorno `MONGODB_URI` (base de datos `liga`).

## Ejecutar
Importa la carpeta como **Existing Maven Project** y ejecuta `LigaFutbolApplication.java`, o `mvn spring-boot:run`.

- Panel principal: http://localhost:8080/
- Vistas web: http://localhost:8080/clubes
- API: http://localhost:8080/api/clubes

Se incluyen `requests.http` y `postman_collection.json` para probar la API.
