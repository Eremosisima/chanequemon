# Guía de Publicación Defensiva — Cipactli: The Lesser Key

Para que este documento sirva como prior art legalmente válido:

## Paso 1: Subir a GitHub

```bash
cd F:\Chanequemon
git init
git add .
git commit -m "Defensive publication: Chanequemon - 2026-06-07"
```

Crear repo público en GitHub: `https://github.com/[TU_USUARIO]/Chanequemon`

```bash
git remote add origin https://github.com/[TU_USUARIO]/Chanequemon.git
git push -u origin main
```

## Paso 2: Archivar en Wayback Machine

1. Ir a: https://web.archive.org/
2. Pegar: `https://github.com/[TU_USUARIO]/Chanequemon`
3. Click: "Save Page Now"
4. Hacer lo mismo con: `https://github.com/[TU_USUARIO]/Chanequemon/blob/main/README.md`
5. Guardar los enlaces de archive.org generados

## Paso 3: (Opcional) Publicar en plataforma de prior art

- **IP.com** (pago, ~$200-500): Indexado directamente en bases de datos de examinadores de patentes
- **Unpatentable.org** (gratis): Publica en blockchain Arweave con timestamp inmutable
- **arXiv.org** (gratis): Para la parte algorítmica/técnica

## Paso 4: Si Nintendo/Nintendo filtra una patente similar

Usar "Third-Party Preissuance Submission" (35 U.S.C. § 122(e)):
1. Localizar la solicitud de patente publicada
2. Enviar el enlace de GitHub + Wayback Machine como prior art
3. El examinador está obligado a considerarlo

## Estructura del Repositorio

```
Chanequemon/
├── README.md              ← Documento principal de publicación defensiva
├── LICENSE                ← CC0 1.0 Universal (dedicación a dominio público)
├── GUIA-PUBLICACION.md    ← Instrucciones de publicación defensiva
├── docs/
│   ├── guia-diseno.md     ← Bestiario completo: 80 criaturas dominio público
│   ├── analisis-patentes.md
│   ├── mecanicas.md       ← Diseño completo de sistemas de juego: recursos, sigilo, captura, sacrificio, Cipactli final
│   ├── narrativa.md       ← Documento narrativo: Cipactli: The Lesser Key
│   └── diagramas/
├── chanequemon-roblox/    ← Implementación Roblox (Luau)
│   ├── default.project.json
│   └── src/
│       ├── server/
│       ├── shared/
│       └── startergui/
└── src/
    └── minecraft-plugin/  ← Paper plugin (Java, mantenido por compatibilidad)
```
