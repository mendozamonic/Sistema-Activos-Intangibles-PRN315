-- Script para simplificar la tabla historial
-- Solo mantendrá un ID auto-generado
-- Ejecutar este script en pgAdmin 4 en la base de datos "proyecto"

-- Paso 1: Eliminar todas las constraints existentes
ALTER TABLE historial DROP CONSTRAINT IF EXISTS fk_historial_intangible;
ALTER TABLE historial DROP CONSTRAINT IF EXISTS fk_historial_detalle;
ALTER TABLE historial DROP CONSTRAINT IF EXISTS fk_historial_reporte;
ALTER TABLE historial DROP CONSTRAINT IF EXISTS pk_historial;
ALTER TABLE historial DROP CONSTRAINT IF EXISTS historial_pkey;

-- Paso 2: Eliminar todas las columnas excepto idhistorial
-- Primero verificamos si existe la columna idhistorial
DO $$
BEGIN
    -- Eliminar todas las columnas que no sean idhistorial
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'historial' AND column_name = 'idintangible') THEN
        ALTER TABLE historial DROP COLUMN idintangible;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'historial' AND column_name = 'iddetalle') THEN
        ALTER TABLE historial DROP COLUMN iddetalle;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'historial' AND column_name = 'idreporte') THEN
        ALTER TABLE historial DROP COLUMN idreporte;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'historial' AND column_name = 'fecha_cuota') THEN
        ALTER TABLE historial DROP COLUMN fecha_cuota;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'historial' AND column_name = 'numero_cuota') THEN
        ALTER TABLE historial DROP COLUMN numero_cuota;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'historial' AND column_name = 'monto') THEN
        ALTER TABLE historial DROP COLUMN monto;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'historial' AND column_name = 'amortizacion_acumulada') THEN
        ALTER TABLE historial DROP COLUMN amortizacion_acumulada;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'historial' AND column_name = 'valor_en_libros') THEN
        ALTER TABLE historial DROP COLUMN valor_en_libros;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'historial' AND column_name = 'costo_original') THEN
        ALTER TABLE historial DROP COLUMN costo_original;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'historial' AND column_name = 'vida_util') THEN
        ALTER TABLE historial DROP COLUMN vida_util;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'historial' AND column_name = 'porcentaje_completado') THEN
        ALTER TABLE historial DROP COLUMN porcentaje_completado;
    END IF;
    
    -- Eliminar otras columnas legacy si existen
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'historial' AND column_name = 'idcompra') THEN
        ALTER TABLE historial DROP COLUMN idcompra;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'historial' AND column_name = 'fecha_compra') THEN
        ALTER TABLE historial DROP COLUMN fecha_compra;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'historial' AND column_name = 'fecha_vencimiento') THEN
        ALTER TABLE historial DROP COLUMN fecha_vencimiento;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'historial' AND column_name = 'idusuario') THEN
        ALTER TABLE historial DROP COLUMN idusuario;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'historial' AND column_name = 'nombre_usuario') THEN
        ALTER TABLE historial DROP COLUMN nombre_usuario;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'historial' AND column_name = 'version') THEN
        ALTER TABLE historial DROP COLUMN version;
    END IF;
END $$;

-- Paso 3: Asegurar que idhistorial existe y es la única columna
-- Si la tabla no tiene idhistorial, la creamos
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'historial' AND column_name = 'idhistorial') THEN
        -- Si la tabla está vacía de columnas, recrearla
        DROP TABLE IF EXISTS historial CASCADE;
        CREATE TABLE historial (
            idhistorial SERIAL PRIMARY KEY
        );
    ELSE
        -- Si existe idhistorial, asegurarnos de que sea PRIMARY KEY y auto-incremental
        -- Verificar si ya es SERIAL o tiene secuencia
        IF NOT EXISTS (
            SELECT 1 FROM information_schema.columns 
            WHERE table_name = 'historial' 
            AND column_name = 'idhistorial' 
            AND is_nullable = 'NO'
        ) THEN
            ALTER TABLE historial ALTER COLUMN idhistorial SET NOT NULL;
        END IF;
        
        -- Crear secuencia si no existe
        IF NOT EXISTS (SELECT 1 FROM pg_sequences WHERE sequencename = 'historial_idhistorial_seq') THEN
            CREATE SEQUENCE historial_idhistorial_seq;
            ALTER TABLE historial ALTER COLUMN idhistorial SET DEFAULT nextval('historial_idhistorial_seq');
            ALTER SEQUENCE historial_idhistorial_seq OWNED BY historial.idhistorial;
        END IF;
        
        -- Asegurar que sea PRIMARY KEY
        IF NOT EXISTS (
            SELECT 1 FROM information_schema.table_constraints 
            WHERE table_name = 'historial' 
            AND constraint_type = 'PRIMARY KEY'
        ) THEN
            ALTER TABLE historial ADD PRIMARY KEY (idhistorial);
        END IF;
    END IF;
END $$;

-- Paso 4: Verificar la estructura final
SELECT column_name, data_type, is_nullable, column_default
FROM information_schema.columns 
WHERE table_name = 'historial' 
ORDER BY ordinal_position;

-- Verificar que solo existe idhistorial
SELECT COUNT(*) as total_columnas
FROM information_schema.columns 
WHERE table_name = 'historial';

