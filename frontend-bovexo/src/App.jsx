import { useState, useEffect } from 'react'
import axios from 'axios'
import { Activity, DollarSign, Send, Search, CheckCircle, Tag, LogIn, UserPlus, LogOut, User } from 'lucide-react'

const TIPOS_ALIMENTO = [
  "MILHO", "SOJA", "FARELO_SOJA", "SORGO", "TRIGO",
  "SUPLEMENTO_MINERAL", "NUCLEO_PROTEICO", "SAL_BRANCO", "UREIA", "SILAGEM_MILHO"
];

function App() {
  const [token, setToken] = useState(localStorage.getItem('@BovExo:token') || null);
  const [loggedUser, setLoggedUser] = useState(localStorage.getItem('@BovExo:user') || null);

  const [isLoginMode, setIsLoginMode] = useState(true);
  const [authForm, setAuthForm] = useState({ username: '', password: '' });
  const [authError, setAuthError] = useState('');
  const [authSuccess, setAuthSuccess] = useState('');

  const [animalIdForm, setAnimalIdForm] = useState('');
  const [feedTypeForm, setFeedTypeForm] = useState('MILHO');
  const [quantityForm, setQuantityForm] = useState('');
  const [statusEnvio, setStatusEnvio] = useState(null);
  const [buscaAnimalId, setBuscaAnimalId] = useState('');
  const [resultadoAnalise, setResultadoAnalise] = useState(null);

  const handleAuth = async (e) => {
    e.preventDefault();
    setAuthError(''); setAuthSuccess('');

    try {
      const endpoint = isLoginMode ? 'http://localhost:8082/auth/login' : 'http://localhost:8082/auth/register';
      const response = await axios.post(endpoint, authForm);

      if (isLoginMode) {
        loginNoSistema(response.data.token, response.data.username);
      } else {
        setAuthSuccess('Conta criada! Faça login para entrar.');
        setIsLoginMode(true);
        setAuthForm({ username: '', password: '' });
      }
    } catch (error) {
      setAuthError(error.response?.data?.erro || 'Erro de conexão com o servidor.');
    }
  };

  const handleGuestLogin = async () => {
    try {
      const response = await axios.post('http://localhost:8082/auth/guest');
      loginNoSistema(response.data.token, response.data.username);
    } catch (error) {
      setAuthError('Servidor de autenticação offline.');
    }
  };

  const loginNoSistema = (jwt, username) => {
    localStorage.setItem('@BovExo:token', jwt);
    localStorage.setItem('@BovExo:user', username);
    setToken(jwt);
    setLoggedUser(username);
  };

  const logout = () => {
    localStorage.removeItem('@BovExo:token');
    localStorage.removeItem('@BovExo:user');
    setToken(null);
    setLoggedUser(null);
    setResultadoAnalise(null); 
  };


  const registrarConsumo = async (e) => {
    e.preventDefault();
    try {
      await axios.post('http://localhost:8082/feeds', {
        animalId: animalIdForm,
        feedType: feedTypeForm,
        quantity: parseFloat(quantityForm)
      }, {
        headers: { Authorization: `Bearer ${token}` }
      });
      setStatusEnvio('sucesso');
      setTimeout(() => setStatusEnvio(null), 3000);
      setQuantityForm('');
    } catch (error) {
      console.error(error);
      setStatusEnvio('erro');
    }
  };

  const buscarHistorico = async () => {
    try {
      const endpoint = buscaAnimalId.trim() === ''
        ? 'http://localhost:8083/analysis'
        : `http://localhost:8083/analysis/${buscaAnimalId}`;
      const response = await axios.get(endpoint);
      setResultadoAnalise(response.data);
    } catch (error) {
      setResultadoAnalise([]);
    }
  };



  if (!token) {
    return (
      <div className="min-h-screen bg-gray-50 flex flex-col justify-center items-center p-4">
        <div className="mb-8 text-center">
          <Activity size={60} className="text-roxo-principal mx-auto mb-4" />
          <h1 className="text-4xl font-bold text-gray-800">BovExo</h1>
          <p className="text-gray-500 mt-2">Nutrição Inteligente</p>
        </div>

        <div className="bg-white p-8 rounded-xl shadow-lg w-full max-w-md border-t-4 border-roxo-principal">
          <div className="flex justify-around mb-6 border-b pb-2">
            <button onClick={() => { setIsLoginMode(true); setAuthError(''); setAuthSuccess(''); }}
              className={`font-bold pb-2 px-4 ${isLoginMode ? 'text-roxo-principal border-b-2 border-roxo-principal' : 'text-gray-400'}`}>
              Login
            </button>
            <button onClick={() => { setIsLoginMode(false); setAuthError(''); setAuthSuccess(''); }}
              className={`font-bold pb-2 px-4 ${!isLoginMode ? 'text-roxo-principal border-b-2 border-roxo-principal' : 'text-gray-400'}`}>
              Criar Conta
            </button>
          </div>

          <form onSubmit={handleAuth} className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700">Usuário</label>
              <input type="text" required value={authForm.username} onChange={(e) => setAuthForm({ ...authForm, username: e.target.value })}
                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-roxo-claro focus:ring focus:ring-roxo-claro p-2 border" />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">Senha</label>
              <input type="password" required value={authForm.password} onChange={(e) => setAuthForm({ ...authForm, password: e.target.value })}
                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-roxo-claro focus:ring focus:ring-roxo-claro p-2 border" />
            </div>

            {authError && <p className="text-red-500 text-sm font-medium text-center bg-red-50 p-2 rounded">{authError}</p>}
            {authSuccess && <p className="text-verde-destaque text-sm font-medium text-center bg-green-50 p-2 rounded">{authSuccess}</p>}

            <button type="submit" className="w-full bg-roxo-principal hover:bg-roxo-claro text-white font-bold py-2 px-4 rounded transition-colors flex justify-center items-center gap-2">
              {isLoginMode ? <><LogIn size={18} /> Entrar</> : <><UserPlus size={18} /> Cadastrar</>}
            </button>
          </form>

          <div className="mt-6 pt-6 border-t border-gray-200">
            <button onClick={handleGuestLogin} type="button" className="w-full bg-gray-100 hover:bg-gray-200 text-gray-700 font-semibold py-2 px-4 rounded transition-colors flex justify-center items-center gap-2 border border-gray-300">
              <User size={18} /> Entrar como Visitante
            </button>
            <p className="text-xs text-gray-400 text-center mt-3">Recomendado para avaliadores técnicos.</p>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-white text-gray-800 p-8 font-sans">
      <header className="mb-10 flex flex-col md:flex-row justify-between items-center bg-gray-50 p-4 rounded-xl border border-gray-200">
        <h1 className="text-2xl font-bold text-roxo-principal flex items-center gap-3">
          <Activity size={28} className="text-roxo-claro" /> BovExo
        </h1>
        <div className="flex items-center gap-4 mt-4 md:mt-0">
          <span className="text-sm font-medium text-gray-600 flex items-center gap-1">
            <User size={16} /> Olá, <strong className="text-roxo-principal">{loggedUser}</strong>
          </span>
          <button onClick={logout} className="text-sm bg-red-100 text-red-600 hover:bg-red-200 py-1 px-3 rounded-md transition-colors flex items-center gap-1">
            <LogOut size={14} /> Sair
          </button>
        </div>
      </header>

      <div className="max-w-5xl mx-auto grid grid-cols-1 md:grid-cols-2 gap-8">
        <div className="bg-white border-2 border-roxo-principal rounded-xl p-6 shadow-lg">
          <h2 className="text-2xl font-semibold text-roxo-principal mb-4 flex items-center gap-2">
            <Send size={24} /> Registrar Alimentação
          </h2>
          <form onSubmit={registrarConsumo} className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700">ID do Animal</label>
              <input type="text" required value={animalIdForm} onChange={(e) => setAnimalIdForm(e.target.value)} className="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-2 border" />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">Insumo</label>
              <select value={feedTypeForm} onChange={(e) => setFeedTypeForm(e.target.value)} className="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-2 border">
                {TIPOS_ALIMENTO.map(tipo => <option key={tipo} value={tipo}>{tipo}</option>)}
              </select>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">Quantidade (Kg)</label>
              <input type="number" step="0.1" required value={quantityForm} onChange={(e) => setQuantityForm(e.target.value)} className="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-2 border" />
            </div>
            <button type="submit" className="w-full bg-roxo-principal hover:bg-roxo-claro text-white font-bold py-2 px-4 rounded">
              Registrar Evento
            </button>
            {statusEnvio === 'sucesso' && <p className="text-verde-destaque font-medium flex items-center gap-1 mt-2 justify-center"><CheckCircle size={18} /> Processado na fila!</p>}
          </form>
        </div>

        <div className="bg-white border-2 border-gray-200 rounded-xl p-6 shadow-md">
          <h2 className="text-2xl font-semibold text-roxo-principal mb-4 flex items-center gap-2">
            <Search size={24} /> Histórico Nutricional
          </h2>
          <div className="flex gap-2 mb-6">
            <input type="text" placeholder="ID do Animal (vazio para todos)" value={buscaAnimalId} onChange={(e) => setBuscaAnimalId(e.target.value)} className="flex-1 rounded-md border-gray-300 shadow-sm p-2 border" />
            <button onClick={buscarHistorico} className="bg-gray-800 hover:bg-gray-900 text-white font-bold py-2 px-4 rounded">Buscar</button>
          </div>
          <div className="space-y-3 overflow-y-auto max-h-96 pr-2">
            {!resultadoAnalise && <p className="text-gray-400 italic text-center mt-10">Consulte o MongoDB para ver as análises</p>}
            {resultadoAnalise && resultadoAnalise.length === 0 && <p className="text-red-500 italic text-center">Nenhum dado encontrado.</p>}
            {resultadoAnalise && resultadoAnalise.map((item, index) => (
              <div key={index} className="border-l-4 border-roxo-claro bg-gray-50 p-3 rounded shadow-sm hover:shadow-md transition-shadow">
                <div className="flex justify-between items-start mb-2">
                  <div>
                    <span className="font-bold text-gray-800 block text-lg">{item.feedType}</span>
                    <span className="text-xs bg-roxo-principal text-white px-2 py-0.5 rounded-full flex items-center gap-1 w-fit mt-1"><Tag size={10} /> Animal: #{item.animalId}</span>
                  </div>
                  <span className="text-[10px] text-gray-500 text-right">{new Date(item.analysisDate).toLocaleString()}</span>
                </div>
                <div className="flex justify-between items-end border-t border-gray-200 pt-2 mt-2">
                  <div className="text-sm"><span className="text-gray-500">Qtd:</span> <span className="font-medium">{item.quantity} kg</span></div>
                  <div className="text-right">
                    <span className="text-[10px] text-gray-400 block">Custo Total</span>
                    <span className="font-bold text-verde-destaque text-lg flex items-center justify-end"><DollarSign size={16} />{item.totalCost.toFixed(2)}</span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  )
}

export default App