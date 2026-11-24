'use strict';

const API = '/api';

const PORTES = { PEQUENO: 'Pequeno', MEDIO: 'Médio', GRANDE: 'Grande' };
const MORADIAS = { CASA: 'Casa', APARTAMENTO: 'Apartamento' };
const STATUS = { 'Disponível': 'Disponível', 'Adotado': 'Adotado', 'Indisponível': 'Indisponível' };

const texto = (v) => (v === null || v === undefined || v === '' ? '—' : String(v));
const data = (v) => {
    if (!v) return '—';
    const [a, m, d] = v.split('-');
    return `${d}/${m}/${a}`;
};

/* ------------------------------------------------------------------
   Configuração de cada recurso: campos do formulário e colunas da tabela
------------------------------------------------------------------- */
const RESOURCES = {
    adocoes: {
        titulo: 'Adoções',
        singular: 'Adoção', genero: 'f',
        descricao: 'Registro de quem adotou qual animal e qual voluntário acompanhou.',
        endpoint: 'adocoes',
        rotulo: (a) => `Adoção #${a.id}`,
        campos: [
            { name: 'dataAdocao', label: 'Data da adoção', type: 'date', required: true },
            { name: 'animal', label: 'Animal', type: 'relation', resource: 'animais', required: true, apenasDisponiveis: true },
            { name: 'adotante', label: 'Adotante', type: 'relation', resource: 'adotantes', required: true },
            { name: 'voluntario', label: 'Voluntário responsável', type: 'relation', resource: 'voluntarios', required: true },
            { name: 'termoAssinado', label: 'Termo de adoção assinado', type: 'checkbox' },
            { name: 'observacoes', label: 'Observações', type: 'textarea', full: true }
        ],
        colunas: [
            { rotulo: 'Data', valor: (r) => data(r.dataAdocao) },
            { rotulo: 'Animal', valor: (r) => (r.animal ? `${r.animal.nome} (${r.animal.especie})` : '—') },
            { rotulo: 'Adotante', valor: (r) => (r.adotante ? r.adotante.nome : '—') },
            { rotulo: 'Voluntário', valor: (r) => (r.voluntario ? r.voluntario.nome : '—') },
            { rotulo: 'Termo', valor: (r) => (r.termoAssinado ? 'Assinado' : 'Pendente'), pill: (r) => (r.termoAssinado ? 'ok' : 'warn') },
            { rotulo: 'Observações', valor: (r) => texto(r.observacoes), longo: true }
        ]
    },

    animais: {
        titulo: 'Animais',
        singular: 'Animal', genero: 'm',
        descricao: 'Animais resgatados e sua situação de adoção.',
        endpoint: 'animais',
        rotulo: (a) => `${a.nome} (${a.especie})`,
        campos: [
            { name: 'nome', label: 'Nome', type: 'text', required: true },
            { name: 'especie', label: 'Espécie', type: 'text', required: true, sugestoes: ['Cachorro', 'Gato'] },
            { name: 'porte', label: 'Porte', type: 'select', options: PORTES, required: true },
            { name: 'idadeEstimada', label: 'Idade estimada (anos)', type: 'number', min: 0 },
            { name: 'statusAdocao', label: 'Status de adoção', type: 'select', options: STATUS, padrao: 'Disponível', required: true },
            { name: 'dataResgate', label: 'Data do resgate', type: 'date' }
        ],
        colunas: [
            { rotulo: 'Nome', valor: (r) => texto(r.nome) },
            { rotulo: 'Espécie', valor: (r) => texto(r.especie) },
            { rotulo: 'Porte', valor: (r) => PORTES[r.porte] || texto(r.porte) },
            { rotulo: 'Idade', valor: (r) => (r.idadeEstimada == null ? '—' : `${r.idadeEstimada} ${r.idadeEstimada === 1 ? 'ano' : 'anos'}`) },
            { rotulo: 'Status', valor: (r) => texto(r.statusAdocao), pill: (r) => tomStatus(r.statusAdocao) },
            { rotulo: 'Resgatado em', valor: (r) => data(r.dataResgate) }
        ]
    },

    adotantes: {
        titulo: 'Adotantes',
        singular: 'Adotante', genero: 'm',
        descricao: 'Pessoas que adotaram ou querem adotar um animal.',
        endpoint: 'adotantes',
        rotulo: (a) => a.nome,
        campos: [
            { name: 'nome', label: 'Nome completo', type: 'text', required: true, full: true },
            { name: 'cpf', label: 'CPF', type: 'text', required: true, placeholder: '000.000.000-00' },
            { name: 'telefone', label: 'Telefone', type: 'tel', placeholder: '(11) 91234-5678' },
            { name: 'endereco', label: 'Endereço', type: 'text', full: true },
            { name: 'tipoMoradia', label: 'Tipo de moradia', type: 'select', options: MORADIAS, required: true }
        ],
        colunas: [
            { rotulo: 'Nome', valor: (r) => texto(r.nome) },
            { rotulo: 'CPF', valor: (r) => texto(r.cpf) },
            { rotulo: 'Telefone', valor: (r) => texto(r.telefone) },
            { rotulo: 'Endereço', valor: (r) => texto(r.endereco), longo: true },
            { rotulo: 'Moradia', valor: (r) => MORADIAS[r.tipoMoradia] || texto(r.tipoMoradia) }
        ]
    },

    voluntarios: {
        titulo: 'Voluntários',
        singular: 'Voluntário', genero: 'm',
        descricao: 'Equipe que cuida dos animais e acompanha as adoções.',
        endpoint: 'voluntarios',
        rotulo: (v) => v.nome,
        campos: [
            { name: 'nome', label: 'Nome completo', type: 'text', required: true, full: true },
            { name: 'email', label: 'E-mail', type: 'email' },
            { name: 'telefone', label: 'Telefone', type: 'tel' },
            { name: 'funcao', label: 'Função', type: 'text', sugestoes: ['Veterinário(a)', 'Cuidador(a)', 'Triagem', 'Coordenação'] }
        ],
        colunas: [
            { rotulo: 'Nome', valor: (r) => texto(r.nome) },
            { rotulo: 'E-mail', valor: (r) => texto(r.email) },
            { rotulo: 'Telefone', valor: (r) => texto(r.telefone) },
            { rotulo: 'Função', valor: (r) => texto(r.funcao) }
        ]
    },

    vacinaaplicada: {
        titulo: 'Vacinas',
        singular: 'Vacina aplicada', genero: 'f',
        descricao: 'Histórico de vacinas aplicadas em cada animal.',
        endpoint: 'vacinaaplicada',
        rotulo: (v) => `Vacina #${v.id}`,
        campos: [
            { name: 'animal', label: 'Animal', type: 'relation', resource: 'animais', required: true, full: true },
            { name: 'nomeVacina', label: 'Vacina', type: 'text', required: true, full: true, sugestoes: ['V8', 'V10', 'Antirrábica', 'V4', 'V5'] },
            { name: 'dataAplicacao', label: 'Data de aplicação', type: 'date', required: true },
            { name: 'proximaDose', label: 'Próxima dose', type: 'date' }
        ],
        colunas: [
            { rotulo: 'Animal', valor: (r) => (r.animal ? `${r.animal.nome} (${r.animal.especie})` : '—') },
            { rotulo: 'Vacina', valor: (r) => texto(r.nomeVacina) },
            { rotulo: 'Aplicada em', valor: (r) => data(r.dataAplicacao) },
            { rotulo: 'Próxima dose', valor: (r) => data(r.proximaDose) }
        ]
    }
};

function tomStatus(s) {
    const v = (s || '').toLowerCase();
    if (v.startsWith('dispon')) return 'ok';
    if (v.startsWith('adotad')) return 'info';
    if (v.startsWith('indispon') || v.includes('trat')) return 'warn';
    return 'muted';
}

/* ------------------------------------------------------------------
   Estado e referências do DOM
------------------------------------------------------------------- */
const $ = (id) => document.getElementById(id);

const estado = {
    atual: 'adocoes',
    linhas: [],
    contagens: {},
    editandoId: null,
    excluindo: null
};

const el = {
    nav: $('nav'), titulo: $('titulo'), descricao: $('descricao'),
    btnNovo: $('btn-novo'), busca: $('busca'), contagem: $('contagem'),
    thead: $('thead'), tbody: $('tbody'), estado: $('estado'),
    modal: $('modal'), form: $('form'), campos: $('campos'),
    modalTitulo: $('modal-titulo'), salvar: $('modal-salvar'),
    confirmar: $('confirmar'), confirmarTexto: $('confirmar-texto'),
    toasts: $('toasts')
};

const recurso = () => RESOURCES[estado.atual];
const novoTitulo = (r) => `${r.genero === 'f' ? 'Nova' : 'Novo'} ${r.singular.toLowerCase()}`;

/* ------------------------------------------------------------------
   Comunicação com a API
------------------------------------------------------------------- */
function mensagemDeErro(status) {
    switch (status) {
        case 400: return 'Dados incorretos. Confira os campos e tente novamente.';
        case 404: return 'Registro não encontrado. Ele pode já ter sido removido.';
        case 409: return 'Não é possível excluir: este registro está vinculado a outros. Remova primeiro os vínculos.';
        default: return `Erro inesperado no servidor (${status}).`;
    }
}

async function api(caminho, opcoes = {}) {
    let res;
    try {
        res = await fetch(`${API}/${caminho}`, {
            headers: { 'Content-Type': 'application/json' },
            ...opcoes
        });
    } catch {
        throw new Error('Não foi possível conectar à API. Verifique se o servidor está rodando.');
    }
    if (!res.ok) throw new Error(mensagemDeErro(res.status));
    const corpo = await res.text();
    const tipo = res.headers.get('content-type') || '';
    return tipo.includes('json') && corpo ? JSON.parse(corpo) : corpo;
}

/* ------------------------------------------------------------------
   Feedback (toasts)
------------------------------------------------------------------- */
function toast(mensagem, erro = false) {
    const t = document.createElement('div');
    t.className = `toast${erro ? ' erro' : ''}`;
    t.textContent = mensagem;
    el.toasts.append(t);
    setTimeout(() => t.remove(), erro ? 6000 : 3200);
}

/* ------------------------------------------------------------------
   Navegação
------------------------------------------------------------------- */
function montarNav() {
    el.nav.replaceChildren();
    for (const [chave, r] of Object.entries(RESOURCES)) {
        const b = document.createElement('button');
        b.type = 'button';
        b.dataset.chave = chave;
        const nome = document.createElement('span');
        nome.textContent = r.titulo;
        const badge = document.createElement('span');
        badge.className = 'badge';
        badge.textContent = estado.contagens[chave] ?? '–';
        b.append(nome, badge);
        b.addEventListener('click', () => trocar(chave));
        el.nav.append(b);
    }
    marcarNav();
}

function marcarNav() {
    for (const b of el.nav.children) {
        if (b.dataset.chave === estado.atual) b.setAttribute('aria-current', 'page');
        else b.removeAttribute('aria-current');
        b.querySelector('.badge').textContent = estado.contagens[b.dataset.chave] ?? '–';
    }
}

function trocar(chave) {
    estado.atual = chave;
    history.replaceState(null, '', `#${chave}`);
    el.busca.value = '';
    const r = recurso();
    el.titulo.textContent = r.titulo;
    el.descricao.textContent = r.descricao;
    el.btnNovo.textContent = `+ ${novoTitulo(r)}`;
    marcarNav();
    carregar();
}

/* ------------------------------------------------------------------
   Listagem (GET)
------------------------------------------------------------------- */
function mostrarEstado(titulo, detalhe, comRetry = false) {
    el.estado.hidden = false;
    el.estado.replaceChildren();
    const s = document.createElement('strong');
    s.textContent = titulo;
    el.estado.append(s, document.createTextNode(detalhe || ''));
    if (comRetry) {
        const b = document.createElement('button');
        b.type = 'button';
        b.className = 'btn ghost';
        b.textContent = 'Tentar novamente';
        b.addEventListener('click', carregar);
        el.estado.append(document.createElement('br'), b);
    }
}

async function carregar() {
    const r = recurso();
    el.tbody.replaceChildren();
    el.thead.replaceChildren();
    el.contagem.textContent = '';
    mostrarEstado('Carregando…');
    try {
        estado.linhas = await api(r.endpoint);
        estado.contagens[estado.atual] = estado.linhas.length;
        marcarNav();
        renderizar();
    } catch (e) {
        estado.linhas = [];
        mostrarEstado('Não foi possível carregar a lista', e.message, true);
    }
}

async function carregarContagens() {
    await Promise.all(Object.entries(RESOURCES).map(async ([chave, r]) => {
        try {
            estado.contagens[chave] = (await api(r.endpoint)).length;
        } catch { /* a contagem fica como "–" */ }
    }));
    marcarNav();
}

function renderizar() {
    const r = recurso();

    const cab = document.createElement('tr');
    const thId = document.createElement('th');
    thId.textContent = '#';
    cab.append(thId);
    for (const c of r.colunas) {
        const th = document.createElement('th');
        th.textContent = c.rotulo;
        cab.append(th);
    }
    const thAc = document.createElement('th');
    thAc.className = 'acoes';
    thAc.textContent = 'Ações';
    cab.append(thAc);
    el.thead.replaceChildren(cab);

    const q = el.busca.value.trim().toLowerCase();
    const visiveis = estado.linhas.filter((l) => !q || r.colunas.some((c) => c.valor(l).toLowerCase().includes(q)));

    el.tbody.replaceChildren();
    el.contagem.textContent = `${visiveis.length} ${visiveis.length === 1 ? 'registro' : 'registros'}`;

    if (visiveis.length === 0) {
        if (estado.linhas.length === 0) mostrarEstado('Nada cadastrado ainda', `Use o botão “${novoTitulo(r)}” para começar.`);
        else mostrarEstado('Nenhum resultado', 'Tente outro termo de busca.');
        return;
    }
    el.estado.hidden = true;

    for (const linha of visiveis) {
        const tr = document.createElement('tr');

        const tdId = document.createElement('td');
        tdId.className = 'id';
        tdId.textContent = linha.id;
        tr.append(tdId);

        for (const c of r.colunas) {
            const td = document.createElement('td');
            if (c.longo) td.className = 'longo';
            const v = c.valor(linha);
            if (c.pill) {
                const p = document.createElement('span');
                p.className = `pill ${c.pill(linha)}`;
                p.textContent = v;
                td.append(p);
            } else {
                td.textContent = v;
                if (c.longo) td.title = v;
            }
            tr.append(td);
        }

        const tdAc = document.createElement('td');
        tdAc.className = 'acoes';
        const editar = document.createElement('button');
        editar.type = 'button';
        editar.className = 'link-btn';
        editar.textContent = 'Editar';
        editar.addEventListener('click', () => abrirFormulario(linha));
        const excluir = document.createElement('button');
        excluir.type = 'button';
        excluir.className = 'link-btn excluir';
        excluir.textContent = 'Excluir';
        excluir.addEventListener('click', () => pedirExclusao(linha));
        tdAc.append(editar, excluir);
        tr.append(tdAc);

        el.tbody.append(tr);
    }
}

/* ------------------------------------------------------------------
   Formulário (POST / PUT)
------------------------------------------------------------------- */
function criarCampo(c, relacoes, linha) {
    const wrap = document.createElement('div');
    wrap.className = `campo${c.full ? ' cheio' : ''}${c.type === 'checkbox' ? ' check cheio' : ''}`;

    const id = `campo-${c.name}`;
    const label = document.createElement('label');
    label.htmlFor = id;
    label.textContent = c.label;
    if (c.required) {
        const s = document.createElement('span');
        s.className = 'obrigatorio';
        s.textContent = ' *';
        label.append(s);
    }

    let input;
    const valor = linha ? linha[c.name] : undefined;

    if (c.type === 'select' || c.type === 'relation') {
        input = document.createElement('select');
        const vazio = document.createElement('option');
        vazio.value = '';
        vazio.textContent = 'Selecione…';
        input.append(vazio);

        if (c.type === 'select') {
            for (const [v, rot] of Object.entries(c.options)) input.append(new Option(rot, v));
            // mantém valores antigos que não estão na lista (ex.: dados cadastrados antes)
            if (valor && !(valor in c.options)) input.append(new Option(valor, valor));
            const inicial = linha ? valor : c.padrao;
            if (inicial) input.value = inicial;
        } else {
            let itens = relacoes[c.resource] || [];
            if (c.apenasDisponiveis) {
                // só animais ainda não adotados e não indisponíveis (mais o animal atual, ao editar)
                itens = itens.filter((i) => {
                    const st = (i.statusAdocao || '').toLowerCase();
                    const livre = !st.startsWith('adotad') && !st.startsWith('indispon');
                    return livre || (valor && valor.id === i.id);
                });
            }
            const rotulo = RESOURCES[c.resource].rotulo;
            for (const item of itens) input.append(new Option(rotulo(item), item.id));
            if (itens.length === 0) vazio.textContent = c.apenasDisponiveis ? 'Nenhum animal disponível' : `Nenhum registro em ${RESOURCES[c.resource].titulo.toLowerCase()}`;
            if (valor && valor.id != null) input.value = String(valor.id);
        }
    } else if (c.type === 'textarea') {
        input = document.createElement('textarea');
        input.value = valor ?? '';
    } else {
        input = document.createElement('input');
        input.type = c.type;
        if (c.type === 'checkbox') input.checked = !!valor;
        else input.value = valor ?? '';
        if (c.min !== undefined) input.min = c.min;
        if (c.placeholder) input.placeholder = c.placeholder;
        if (c.sugestoes) {
            const dl = document.createElement('datalist');
            dl.id = `lista-${c.name}`;
            for (const s of c.sugestoes) dl.append(new Option(s));
            input.setAttribute('list', dl.id);
            wrap.append(dl);
        }
    }

    input.id = id;
    input.name = c.name;
    if (c.required && c.type !== 'checkbox') input.required = true;

    if (c.type === 'checkbox') wrap.append(input, label);
    else wrap.append(label, input);
    return wrap;
}

async function abrirFormulario(linha = null) {
    const r = recurso();
    estado.editandoId = linha ? linha.id : null;
    el.modalTitulo.textContent = linha ? `Editar ${r.singular.toLowerCase()}` : novoTitulo(r);
    el.campos.replaceChildren();

    // carrega as listas usadas nos campos de relação (animal, adotante, voluntário)
    const relacoes = {};
    const recursosNecessarios = [...new Set(r.campos.filter((c) => c.type === 'relation').map((c) => c.resource))];
    try {
        await Promise.all(recursosNecessarios.map(async (nome) => {
            relacoes[nome] = await api(RESOURCES[nome].endpoint);
        }));
    } catch (e) {
        toast(e.message, true);
        return;
    }

    for (const c of r.campos) el.campos.append(criarCampo(c, relacoes, linha));
    el.salvar.disabled = false;
    el.salvar.textContent = 'Salvar';
    el.modal.showModal();
    el.campos.querySelector('input:not([type="checkbox"]), select, textarea')?.focus();
}

function lerFormulario(r) {
    const corpo = {};
    for (const c of r.campos) {
        const campo = el.form.elements[c.name];
        if (c.type === 'checkbox') corpo[c.name] = campo.checked;
        else if (c.type === 'relation') corpo[c.name] = campo.value ? { id: Number(campo.value) } : null;
        else if (c.type === 'number') corpo[c.name] = campo.value === '' ? null : Number(campo.value);
        else if (c.type === 'date') corpo[c.name] = campo.value || null;
        else corpo[c.name] = campo.value.trim() === '' ? null : campo.value.trim();
    }
    return corpo;
}

function validar(r) {
    let primeiro = null;
    for (const c of r.campos) {
        const campo = el.form.elements[c.name];
        const invalido = !campo.checkValidity();
        campo.classList.toggle('invalido', invalido);
        if (invalido && !primeiro) primeiro = campo;
    }
    if (primeiro) {
        primeiro.focus();
        toast('Preencha os campos obrigatórios corretamente.', true);
        return false;
    }
    return true;
}

async function salvar(evento) {
    evento.preventDefault();
    const r = recurso();
    if (!validar(r)) return;

    const editando = estado.editandoId !== null;
    el.salvar.disabled = true;
    el.salvar.textContent = 'Salvando…';
    try {
        await api(editando ? `${r.endpoint}/${estado.editandoId}` : r.endpoint, {
            method: editando ? 'PUT' : 'POST',
            body: JSON.stringify(lerFormulario(r))
        });
        el.modal.close();
        toast(editando ? `${r.singular} atualizad${r.genero === 'f' ? 'a' : 'o'} com sucesso.` : `${r.singular} salv${r.genero === 'f' ? 'a' : 'o'} com sucesso.`);
        await carregar();
    } catch (e) {
        toast(e.message, true);
        el.salvar.disabled = false;
        el.salvar.textContent = 'Salvar';
    }
}

/* ------------------------------------------------------------------
   Exclusão (DELETE)
------------------------------------------------------------------- */
function pedirExclusao(linha) {
    const r = recurso();
    estado.excluindo = linha;
    el.confirmarTexto.textContent = `Deseja realmente excluir ${r.genero === 'f' ? 'esta' : 'este'} ${r.singular.toLowerCase()} (${r.rotulo(linha)})? Esta ação não pode ser desfeita.`;
    el.confirmar.showModal();
}

async function confirmarExclusao() {
    const r = recurso();
    const linha = estado.excluindo;
    el.confirmar.close();
    if (!linha) return;
    try {
        await api(`${r.endpoint}/${linha.id}`, { method: 'DELETE' });
        toast(`${r.singular} excluíd${r.genero === 'f' ? 'a' : 'o'} com sucesso.`);
        await carregar();
    } catch (e) {
        toast(e.message, true);
    }
}

/* ------------------------------------------------------------------
   Eventos e inicialização
------------------------------------------------------------------- */
el.btnNovo.addEventListener('click', () => abrirFormulario());
el.busca.addEventListener('input', renderizar);
el.form.addEventListener('submit', salvar);
$('modal-cancelar').addEventListener('click', () => el.modal.close());
$('modal-fechar').addEventListener('click', () => el.modal.close());
$('confirmar-nao').addEventListener('click', () => el.confirmar.close());
$('confirmar-sim').addEventListener('click', confirmarExclusao);

const inicial = location.hash.slice(1);
if (RESOURCES[inicial]) estado.atual = inicial;

montarNav();
trocar(estado.atual);
carregarContagens();
