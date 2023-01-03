using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Security.Cryptography;
using System.Text;
using System.Web;
using System.Web.Services;

namespace WebServiceIPCAGym.Services
{
    /// <summary>
    /// WebService com um conjunto de WebMethods exemplo para diferentes requests do IPCAGym
    /// </summary>
    [WebService(Namespace = "www.ipca.pt",
                Description = "WebService exemplo para a funcionalidades do IPCAGym",
                Name = "IPCAGym")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    public class IPCAGymWS : WebService
    {
        [WebMethod]
        public string HelloWorld()
        {
            return "Hello World";
        }

        /// <summary>
        /// Remoção de uma classificação da base de dados pelo seu ID
        /// </summary>
        /// <param name="ID_Classificação">ID da classificação a ser removida</param>
        /// <returns>True se a remoção foi bem sucedida, false em caso de erro</returns>
        /// <exception cref="SqlException">Ocorre quando um erro ou warning é retornado da base de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        [WebMethod]
        public bool DeleteClassificacaoService(int ID_Classificação)
        {
            string query = @"
                            delete from dbo.Classificacao 
                            where id_avaliacao = @id_avaliacao";
            string connectionString = "data source=DESKTOP-8AIBMTC;initial catalog=ipcagym;trusted_connection=true";

            try
            {

                using (SqlConnection databaseConnection = new SqlConnection(connectionString))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_avaliacao", ID_Classificação);
                        int rowsAffected = myCommand.ExecuteNonQuery();

                        if (rowsAffected == 0) return false;

                        databaseConnection.Close();
                    }
                }

                return true;
            }
            catch (SqlException ex)
            {
                Console.WriteLine("Erro/Warning no SQLServer: " + ex.Message);
                return false;
            }
            catch (ArgumentNullException ex)
            {
                Console.WriteLine("Parâmetro inserido é nulo: " + ex.Message);
                return false;
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
                return false;
            }
        }

        public class Cliente
        {
            public int id_cliente { get; set; }
            public int id_ginasio { get; set; }
            public int id_plano_nutricional { get; set; }
            public string nome { get; set; }
            public string mail { get; set; }
            public int telemovel { get; set; }
            public string pass_salt { get; set; }
            public string pass_hash { get; set; }
            public double peso { get; set; }
            public int altura { get; set; }
            public double gordura { get; set; }
            public string foto_perfil { get; set; }
            public string estado { get; set; }

        }

        /// <summary>
        /// Leitura dos dados de todos os clientes da base de dados do IPCAGYM
        /// </summary>
        /// <returns>Lista de clientes se uma leitura bem sucedida, null em caso de erro</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="InvalidOperationException">Trata o caso em que ocorreu um erro de leitura dos dados</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="IndexOutOfRangeException">Trata o caso em que o índice da coluna da base de dados acessado é inválido</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        [WebMethod]
        public List<Cliente> GetAllClientesService()
        {
            string query = @"select * from dbo.Cliente";
            string connectionString = "data source=DESKTOP-8AIBMTC;initial catalog=ipcagym;trusted_connection=true";
            List<Cliente> clientes = new List<Cliente>();

            try
            {
                SqlDataReader dataReader;
                using (SqlConnection databaseConnection = new SqlConnection(connectionString))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        dataReader = myCommand.ExecuteReader();
                        while (dataReader.Read())
                        {
                            Cliente cliente = new Cliente();

                            cliente.id_cliente = Convert.ToInt32(dataReader["id_cliente"]);
                            cliente.id_ginasio = Convert.ToInt32(dataReader["id_ginasio"]);
                            if (!Convert.IsDBNull(dataReader["id_plano_nutricional"]))
                            {
                                cliente.id_plano_nutricional = Convert.ToInt32(dataReader["id_plano_nutricional"]);
                            }
                            else
                            {
                                cliente.id_plano_nutricional = -1;
                            }
                            cliente.nome = dataReader["nome"].ToString();
                            cliente.mail = dataReader["mail"].ToString();
                            cliente.telemovel = Convert.ToInt32(dataReader["telemovel"]);
                            cliente.pass_salt = "It's a secret!";
                            cliente.pass_hash = "Already told you that it's a secret!";
                            if (!Convert.IsDBNull(dataReader["peso"]))
                            {
                                cliente.peso = Convert.ToDouble(dataReader["peso"]);
                            }
                            else
                            {
                                cliente.peso = -1;
                            }
                            if (!Convert.IsDBNull(dataReader["altura"]))
                            {
                                cliente.altura = Convert.ToInt32(dataReader["altura"]);
                            }
                            else
                            {
                                cliente.altura = -1;
                            }
                            if (!Convert.IsDBNull(dataReader["gordura"]))
                            {
                                cliente.gordura = Convert.ToDouble(dataReader["gordura"]);
                            }
                            else
                            {
                                cliente.gordura = -1;
                            }
                            if (!Convert.IsDBNull(dataReader["foto_perfil"]))
                            {
                                cliente.foto_perfil = dataReader["foto_perfil"].ToString();
                            }
                            else
                            {
                                cliente.foto_perfil = null;
                            }
                            cliente.estado = dataReader["estado"].ToString();

                            clientes.Add(cliente);
                        }

                        dataReader.Close();
                        databaseConnection.Close();
                    }
                }

                return clientes;
            }
            catch (SqlException ex)
            {
                Console.WriteLine("Erro na conexão com a base de dados: " + ex.Message);
                return null;
            }
            catch (InvalidCastException ex)
            {
                Console.WriteLine("Erro na conversão de dados: " + ex.Message);
                return null;
            }
            catch (InvalidOperationException ex)
            {
                Console.WriteLine("Erro de leitura dos dados: " + ex.Message);
                return null;
            }
            catch (FormatException ex)
            {
                Console.WriteLine("Erro de tipo de dados: " + ex.Message);
                return null;
            }
            catch (IndexOutOfRangeException ex)
            {
                Console.WriteLine("Erro de acesso a uma coluna da base de dados: " + ex.Message);
                return null;
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
                return null;
            }
        }

        /// <summary>
        /// Atualiza os dados de um ginásio através do seu id na base de dados
        /// </summary>
        /// <param name="targetID">ID da ginásio a ser atualizada</param>
        /// <param name="contacto">Valor para o contacto do ginasio</param>
        /// <param name="estado">Valor para o estado do ginasio</param>
        /// <param name="foto">Valor para a source da foto do ginasio</param>
        /// <param name="instituicao">Valor para a instituicao do ginasio</param>
        /// <param name="lotacao">Valor para a lotacao do ginasio</param>
        /// <param name="lotacaoMax">Valor para o a lotacao maxima do ginasio</param>
        /// <returns>True se a atualização dos dados foi bem sucedida, false caso contrário</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="ArgumentException">Valores em falta para ser inseridos</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        [WebMethod]
        public bool PatchGinasioService(int targetID, string instituicao, int contacto, string foto, string estado, int lotacao, int lotacaoMax)
        {
            string queryPatch = @"
                            update dbo.Ginasio 
                            set instituicao = @instituicao, 
                            contacto = @contacto, 
                            foto_ginasio = @foto_ginasio, 
                            estado = @estado,
                            lotacao = @lotacao,
                            lotacaoMax = @lotacaoMax
                            where id_ginasio = @id_ginasio";

            string queryGet = @"select * from dbo.Ginasio where id_ginasio = @id_ginasio";
            string connectionString = "data source=DESKTOP-8AIBMTC;initial catalog=ipcagym;trusted_connection=true";

            string instituicaoAtual;
            int contactoAtual;
            string foto_ginasioAtual;
            string estadoAtual;
            int lotacaoAtual;
            int lotacaoMaxAtual;

            try
            {
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(connectionString))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommandAux = new SqlCommand(queryGet, databaseConnection))
                    {
                        myCommandAux.Parameters.AddWithValue("id_ginasio", targetID);

                        using (SqlDataReader reader = myCommandAux.ExecuteReader())
                        {
                            reader.Read();

                            instituicaoAtual = reader.GetString(1);
                            contactoAtual = reader.GetInt32(2);
                            foto_ginasioAtual = string.Empty;
                            if (!Convert.IsDBNull(reader["foto_ginasio"]))
                            {
                                foto_ginasioAtual = reader.GetString(3);
                            }
                            else
                            {
                                foto_ginasioAtual = null;
                            }

                            estadoAtual = reader.GetString(4);
                            lotacaoAtual = reader.GetInt32(5);
                            lotacaoMaxAtual = reader.GetInt32(6);

                            reader.Close();
                        }
                    }

                    using (SqlCommand myCommand = new SqlCommand(queryPatch, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_ginasio", targetID);
                        myCommand.Parameters.AddWithValue("instituicao", !string.IsNullOrEmpty(instituicao) ? instituicao : instituicaoAtual);
                        myCommand.Parameters.AddWithValue("contacto", contacto == null ? contacto : contactoAtual);

                        if (!string.IsNullOrEmpty(foto) && !string.IsNullOrEmpty(foto_ginasioAtual))
                            myCommand.Parameters.AddWithValue("foto_ginasio", !string.IsNullOrEmpty(foto) ? foto : foto_ginasioAtual);
                        else
                            myCommand.Parameters.AddWithValue("foto_ginasio", DBNull.Value);

                        myCommand.Parameters.AddWithValue("estado", !string.IsNullOrEmpty(estado) ? estado : estadoAtual);
                        myCommand.Parameters.AddWithValue("lotacao", lotacao == null ? lotacao : lotacaoAtual);
                        myCommand.Parameters.AddWithValue("lotacaoMax", lotacaoMax == null ? lotacaoMax : lotacaoMaxAtual);

                        dataReader = myCommand.ExecuteReader();

                        dataReader.Close();
                        databaseConnection.Close();
                    }
                }

                return true;
            }
            catch (SqlException ex)
            {
                Console.WriteLine("Erro na conexao com a base de dados: " + ex.Message);
                return false;
            }
            catch (InvalidCastException ex)
            {
                Console.WriteLine("Erro na conversao de dados: " + ex.Message);
                return false;
            }
            catch (FormatException ex)
            {
                Console.WriteLine("Erro de tipo de dados: " + ex.Message);
                return false;
            }
            catch (ArgumentNullException ex)
            {
                Console.WriteLine("Erro de parametro inserido nulo: " + ex.Message);
                return false;
            }
            catch (ArgumentException ex)
            {
                Console.WriteLine("Valores em falta para ser inseridos: " + ex.Message);
                return false;
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
                return false;
            }
        }

        /// <summary>
        /// Class auxiliar para a receção de dados de login
        /// </summary>
        class LoginModel
        {
            public bool role;
            public int code;
            public string password;
            public string hash_pass;

            public LoginModel() { }

            public LoginModel(bool role, int code, string password, string hash_pass)
            {
                this.role = role;
                this.code = code;
                this.password = password;
                this.hash_pass = hash_pass;
            }
        }

        /// <summary>
        /// LoginWS de um funcionário
        /// </summary>
        /// <param name="codeInput">Codigo de autenticacao para efetuar login do funcionario</param>
        /// <param name="passwordInput">Password de autenticacao para efetuar login do funcionario</param>
        /// <returns>Role do funcionario em caso de sucesso</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidOperationException">Ocorre quando o codigo do funcionario nao está atribuido</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        [WebMethod]
        public string LoginFuncionarioService(string codeInput, string passwordInput)
        {
            string query = @"
                            select * from dbo.Funcionario 
                            where codigo = @codigo and estado != 'Inativo'";

            string connectionString = "data source=DESKTOP-8AIBMTC;initial catalog=ipcagym;trusted_connection=true";
            try
            {
                using (SqlConnection databaseConnection = new SqlConnection(connectionString))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("codigo", codeInput);
                        using (SqlDataReader reader = myCommand.ExecuteReader())
                        {
                            reader.Read();

                            string result = string.Empty;
                            LoginModel account = new LoginModel();

                            account.role = reader.GetBoolean(3);
                            account.code = reader.GetInt32(4);
                            account.password = reader.GetString(5);
                            account.hash_pass = reader.GetString(6);
                            string auxForAdmin = reader.GetString(2);

                            reader.Close();
                            databaseConnection.Close();

                            if (!VerifyPasswordHash(passwordInput, Convert.FromBase64String(account.hash_pass), Convert.FromBase64String(account.password)))
                            {
                                throw new ArgumentException("Password Errada.", "conta");
                            }
                            else
                            {
                                if (account.role == true)
                                {
                                    if (auxForAdmin == "adminaccount") result = "É admin!";
                                    else result = "É gerente!";
                                }
                                else
                                {
                                    result = "É funcionário";
                                }
                            }

                            Console.WriteLine(result);
                            return result;
                        }
                    }

                }
            }
            catch (InvalidOperationException ex)
            {
                Console.WriteLine("Funcionário não existe\n" + ex.Message);
                return "Erro na autenticação";
            }
            catch (SqlException ex)
            {
                Console.WriteLine("Erro na conexão com a base de dados: " + ex.Message);
                return "Erro na autenticação";
            }
            catch (ArgumentNullException ex)
            {
                Console.WriteLine("Erro de parametro inserido nulo: " + ex.Message);
                return "Erro na autenticação";
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
                return "Erro na autenticação";
            }
        }

        /// <summary>
        /// Método auxiliar para verificar se a password está correta
        /// </summary>
        /// <param name="password">Password inserida</param>
        /// <param name="passwordHash">Password encriptada</param>
        /// <param name="passwordSalt">Password normal</param>
        /// <returns>Resultado da operação em Boolean</returns>
        public static bool VerifyPasswordHash(string password, byte[] passwordHash, byte[] passwordSalt)
        {
            using (var hmac = new HMACSHA512(passwordSalt))
            {
                var computeHash = hmac.ComputeHash(Encoding.UTF8.GetBytes(password));
                return computeHash.SequenceEqual(passwordHash);
            }
        }
    }
}
