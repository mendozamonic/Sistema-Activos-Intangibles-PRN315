-- Script para modificar la tabla historial
-- Ejecutar este script en pgAdmin 4 en la base de datos "proyecto"

-- Paso 1: Eliminar la columna idcompra si no la usas
ALTER TABLE historial DROP COLUMN IF EXISTS idcompra;

-- Paso 2: Eliminar constraints existentes si existen (para evitar errores)
ALTER TABLE historial DROP CONSTRAINT IF EXISTS fk_historial_intangible;
ALTER TABLE historial DROP CONSTRAINT IF EXISTS fk_historial_detalle;
ALTER TABLE historial DROP CONSTRAINT IF EXISTS fk_historial_reporte;

-- Paso 3: Agregar las nuevas columnas necesarias
ALTER TABLE historial 
    ADD COLUMN IF NOT EXISTS idintangible VARCHAR(10),
    ADD COLUMN IF NOT EXISTS iddetalle VARCHAR(10),
    ADD COLUMN IF NOT EXISTS idreporte VARCHAR(10),
    ADD COLUMN IF NOT EXISTS fecha_cuota DATE,
    ADD COLUMN IF NOT EXISTS numero_cuota INTEGER,
    ADD COLUMN IF NOT EXISTS monto DECIMAL(10,2),
    ADD COLUMN IF NOT EXISTS amortizacion_acumulada DECIMAL(10,2),
    ADD COLUMN IF NOT EXISTS valor_en_libros DECIMAL(10,2),
    ADD COLUMN IF NOT EXISTS costo_original DECIMAL(10,2),
    ADD COLUMN IF NOT EXISTS vida_util VARCHAR(50),
    ADD COLUMN IF NOT EXISTS porcentaje_completado DECIMAL(5,2);

-- Paso 4: Agregar foreign keys para mantener integridad referencial
-- Solo agregar si las tablas referenciadas existen
DO $$
BEGIN
    -- Foreign key a intangible
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'intangible') THEN
        IF NOT EXISTS (SELECT 1 FROM information_schema.table_constraints 
                       WHERE constraint_name = 'fk_historial_intangible' 
                       AND table_name = 'historial') THEN
            ALTER TABLE historial 
            ADD CONSTRAINT fk_historial_intangible 
            FOREIGN KEY (idintangible) REFERENCES intangible(idintangible);
        END IF;
    END IF;

    -- Foreign key a detalleamortizacion
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'detalleamortizacion') THEN
        IF NOT EXISTS (SELECT 1 FROM information_schema.table_constraints 
                       WHERE constraint_name = 'fk_historial_detalle' 
                       AND table_name = 'historial') THEN
            ALTER TABLE historial 
            ADD CONSTRAINT fk_historial_detalle 
            FOREIGN KEY (iddetalle) REFERENCES detalleamortizacion(iddetalle);
        END IF;
    END IF;

    -- Foreign key a reporte (solo si la tabla reporte existe)
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'reporte') THEN
        IF NOT EXISTS (SELECT 1 FROM information_schema.table_constraints 
                       WHERE constraint_name = 'fk_historial_reporte' 
                       AND table_name = 'historial') THEN
            ALTER TABLE historial 
            ADD CONSTRAINT fk_historial_reporte 
            FOREIGN KEY (idreporte) REFERENCES reporte(idreporte);
        END IF;
    END IF;
END $$;

-- Verificar que las columnas se agregaron correctamente
SELECT column_name, data_type, character_maximum_length 
FROM information_schema.columns 
WHERE table_name = 'historial' 
ORDER BY ordinal_position;

