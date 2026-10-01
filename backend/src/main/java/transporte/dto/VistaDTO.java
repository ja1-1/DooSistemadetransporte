package transporte.dto;

public sealed interface VistaDTO
        permits ConductorDTO,
                VehiculoDTO,
                UsuarioDTO,
                AsignacionDTO,
                RutaDTO,
                ProgramacionRutaDTO,
                SalidaDTO,
                IncidenciaDTO {}
