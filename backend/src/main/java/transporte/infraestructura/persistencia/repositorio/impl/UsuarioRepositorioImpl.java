package transporte.infraestructura.persistencia.repositorio.impl;

import transporte.dao.datos.entidad.UsuarioDAO;
import transporte.dao.factoria.DAOFactory;
import transporte.dominio.UsuarioDominio;
import transporte.dominio.consulta.FiltroUsuario;
import transporte.dominio.repositorio.UsuarioRepositorio;
import transporte.dominio.valor.Pagina;
import transporte.entidad.UsuarioEntidad;
import transporte.infraestructura.persistencia.assembler.impl.UsuarioEntidadAssembler;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UsuarioRepositorioImpl implements UsuarioRepositorio {
    private final UsuarioDAO dao;
    private final UsuarioEntidadAssembler assembler;

    public UsuarioRepositorioImpl(DAOFactory fabrica, UsuarioEntidadAssembler assembler) {
        dao = fabrica.usuario();
        this.assembler = assembler;
    }

    private UsuarioDominio reconstruir(UsuarioEntidad usuarioEntidad) {
        return assembler.convertirADominio(usuarioEntidad);
    }

    @Override
    @Transactional
    public void guardar(UsuarioDominio usuario) {
        dao.guardar(assembler.convertirAEntidad(usuario));
    }

    @Override
    public Optional<UsuarioDominio> buscarPorId(UUID id) {
        return dao.buscarPorId(id).map(this::reconstruir);
    }

    @Override
    public List<UsuarioDominio> buscar(FiltroUsuario filtro, Pagina pagina) {
        return dao.buscar(filtro, pagina).stream().map(this::reconstruir).toList();
    }

    @Override
    public List<UsuarioDominio> relacionados(FiltroUsuario filtro) {
        return dao.relacionados(filtro).stream().map(this::reconstruir).toList();
    }

    @Override
    public long contar(FiltroUsuario filtro) {
        return dao.contar(filtro);
    }
}
