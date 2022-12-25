using LayerBOL.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using System.Data.SqlClient;
using System.Security.Claims;

namespace LayerDAL.Services
{
    public class FuncionarioService
    {
        #region CRUD SERVICES

        /// <summary>
        /// Leitura dos dados de todos os funcionarios da base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <returns>Lista de funcionarios se uma leitura bem sucedida, null em caso de erro</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="InvalidOperationException">Trata o caso em que ocorreu um erro de leitura dos dados</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="IndexOutOfRangeException">Trata o caso em que o índice da coluna da base de dados acessado é inválido</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<List<Funcionario>> GetAllService(string sqlDataSource)
        {
            string query = @"select * from dbo.Funcionario";

            try
            {
                List<Funcionario> funcionarios = new List<Funcionario>();
                SqlDataReader dataReader;
                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        dataReader = myCommand.ExecuteReader();
                        while (dataReader.Read())
                        {
                            Funcionario funcionario = new Funcionario();

                            funcionario.id_funcionario = Convert.ToInt32(dataReader["id_funcionario"]);
                            funcionario.id_ginasio = Convert.ToInt32(dataReader["id_ginasio"]);
                            funcionario.nome = dataReader["nome"].ToString();
                            funcionario.is_admin = Convert.ToBoolean(dataReader["is_admin"]);
                            funcionario.codigo = Convert.ToInt32(dataReader["codigo"]);
                            funcionario.pass_salt = dataReader["pass_salt"].ToString();
                            funcionario.pass_hash = dataReader["pass_hash"].ToString();
                            funcionario.estado = dataReader["estado"].ToString();

                            funcionarios.Add(funcionario);
                        }

                        dataReader.Close();
                        databaseConnection.Close();
                    }
                }
                return funcionarios;
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
                Console.WriteLine(ex.Message);
                return null;
            }
        }

        /// <summary>
        /// Leitura dos dados de todos os funcionarios ativos da base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <returns>Lista de funcionarios se uma leitura bem sucedida, null em caso de erro</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="InvalidOperationException">Trata o caso em que ocorreu um erro de leitura dos dados</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="IndexOutOfRangeException">Trata o caso em que o índice da coluna da base de dados acessado é inválido</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<List<Funcionario>> GetAllAtivoService(string sqlDataSource)
        {
            string query = @"select * from dbo.Funcionario where estado != 'Inativo'";

            try
            {
                List<Funcionario> funcionarios = new List<Funcionario>();
                SqlDataReader dataReader;
                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        dataReader = myCommand.ExecuteReader();
                        while (dataReader.Read())
                        {
                            Funcionario funcionario = new Funcionario();

                            funcionario.id_funcionario = Convert.ToInt32(dataReader["id_funcionario"]);
                            funcionario.id_ginasio = Convert.ToInt32(dataReader["id_ginasio"]);
                            funcionario.nome = dataReader["nome"].ToString();
                            funcionario.is_admin = Convert.ToBoolean(dataReader["is_admin"]);
                            funcionario.codigo = Convert.ToInt32(dataReader["codigo"]);
                            funcionario.pass_salt = dataReader["pass_salt"].ToString();
                            funcionario.pass_hash = dataReader["pass_hash"].ToString();
                            funcionario.estado = dataReader["estado"].ToString();

                            funcionarios.Add(funcionario);
                        }

                        dataReader.Close();
                        databaseConnection.Close();
                    }
                }
                return funcionarios;
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
                Console.WriteLine(ex.Message);
                return null;
            }
        }


        /// <summary>
        /// Leitura dos dados de uma funcionário através do seu id na base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <param name="targetID">ID do funcionário a ser lido</param>
        /// <returns>Atividade se uma leitura bem sucedida, ou null em caso de erro</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="InvalidOperationException">Trata o caso em que ocorreu um erro de leitura dos dados</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="IndexOutOfRangeException">Trata o caso em que o índice da coluna da base de dados acessado é inválido</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<Funcionario> GetByIDService(string sqlDataSource, int targetID)
        {
            string query = @"select * from dbo.Funcionario where id_funcionario = @id_funcionario and estado != 'Inativo'";

            try
            {
                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_funcionario", targetID);

                        using (SqlDataReader reader = myCommand.ExecuteReader())
                        {
                            reader.Read();

                            Funcionario targetFuncionario = new Funcionario();
                            targetFuncionario.id_funcionario = reader.GetInt32(0);
                            targetFuncionario.id_ginasio = reader.GetInt32(1);
                            targetFuncionario.nome = reader.GetString(2);
                            targetFuncionario.is_admin = reader.GetBoolean(3);
                            targetFuncionario.codigo = reader.GetInt32(4);
                            targetFuncionario.pass_salt = reader.GetString(5);
                            targetFuncionario.pass_hash = reader.GetString(6);
                            targetFuncionario.estado = reader.GetString(7);

                            reader.Close();
                            databaseConnection.Close();

                            return targetFuncionario;
                        }
                    }
                }
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
            catch (ArgumentNullException ex)
            {
                Console.WriteLine("Erro de parametro inserido nulo: " + ex.Message);
                return null;
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
                return null;
            }
        }

        /// <summary>
        /// Inserção dos dados de um novo funcionário na base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <param name="newAtividade">Objeto com os dados do novo funcionário</param>
        /// <returns>True se a escrita dos dados foi bem sucedida, false em caso de erro.</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<bool> PostService(string sqlDataSource, Funcionario newFuncionario)
        {
            string query = @"
                            insert into dbo.Funcionario (id_ginasio, nome, is_admin, codigo, pass_salt, pass_hash, estado)
                            values (@id_ginasio, @nome, @is_admin, @codigo, @pass_salt, @pass_hash, @estado)";

            try
            {
                SqlDataReader dataReader;
                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_ginasio", newFuncionario.id_ginasio);
                        myCommand.Parameters.AddWithValue("nome", newFuncionario.nome);
                        myCommand.Parameters.AddWithValue("is_admin", newFuncionario.is_admin);
                        myCommand.Parameters.AddWithValue("codigo", newFuncionario.codigo);

                        PasswordEncryption.CreatePasswordHash(newFuncionario.pass_salt, out byte[] passwordHash, out byte[] passwordSalt);
                        newFuncionario.pass_hash = Convert.ToBase64String(passwordHash);
                        newFuncionario.pass_salt = Convert.ToBase64String(passwordSalt);

                        myCommand.Parameters.AddWithValue("pass_hash", newFuncionario.pass_hash);
                        myCommand.Parameters.AddWithValue("pass_salt", newFuncionario.pass_salt);

                        myCommand.Parameters.AddWithValue("estado", newFuncionario.estado);

                        dataReader = myCommand.ExecuteReader();

                        dataReader.Close();
                        databaseConnection.Close();
                    }
                }

                return true;
            }
            catch (SqlException ex)
            {
                Console.WriteLine("Erro na conexão com a base de dados: " + ex.Message);
                return false;
            }
            catch (InvalidCastException ex)
            {
                Console.WriteLine("Erro na conversão de dados: " + ex.Message);
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
            catch (Exception ex)
            {
                Console.Write(ex.ToString());
                return false;
            }
        }

        /// <summary>
        /// Atualiza os dados de um funcionário através do seu id na base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <param name="funcionario">Objeto com os novos dados do funcionário</param>
        /// <param name="targetID">ID do funcionário a ser atualizado</param>
        /// <returns>True se a atualização dos dados foi bem-sucedida, false caso contrário</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<bool> PatchService(string sqlDataSource, Funcionario funcionario, int targetID)
        {
            string query = @"
                            update dbo.Funcionario 
                            set id_ginasio = @id_ginasio, 
                            nome = @nome, 
                            is_admin = @is_admin,
                            codigo = @codigo,
                            estado = @estado
                            where id_funcionario = @id_funcionario";

            try
            {
                Funcionario funcionarioAtual = await GetByIDService(sqlDataSource, targetID);
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_funcionario", funcionario.id_funcionario != 0 ? funcionario.id_funcionario : funcionarioAtual.id_funcionario);
                        myCommand.Parameters.AddWithValue("id_ginasio", funcionario.id_ginasio != 0 ? funcionario.id_ginasio : funcionarioAtual.id_ginasio);
                        myCommand.Parameters.AddWithValue("nome", !string.IsNullOrEmpty(funcionario.nome) ? funcionario.nome : funcionarioAtual.nome);
                        myCommand.Parameters.AddWithValue("is_admin", funcionario.is_admin != null ? funcionario.is_admin : funcionarioAtual.is_admin);
                        myCommand.Parameters.AddWithValue("codigo", funcionario.codigo != 0 ? funcionario.codigo : funcionarioAtual.codigo);
                        myCommand.Parameters.AddWithValue("estado", !string.IsNullOrEmpty(funcionario.estado) ? funcionario.estado : funcionarioAtual.estado);

                        dataReader = myCommand.ExecuteReader();

                        dataReader.Close();
                        databaseConnection.Close();
                    }
                }

                return true;
            }
            catch (SqlException ex)
            {
                Console.WriteLine("Erro na conexão com a base de dados: " + ex.Message);
                return false;
            }
            catch (InvalidCastException ex)
            {
                Console.WriteLine("Erro na conversão de dados: " + ex.Message);
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
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
                return false;
            }
        }

        /// <summary>
        /// Remoção de um funcionário da base de dados pelo seu ID
        /// </summary>
        /// <param name="sqlDataSource">String de conexão com a base de dados</param>
        /// <param name="targetID">ID da funcionário a ser removida</param>
        /// <returns>True se a remoção foi bem sucedida, false em caso de erro</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<bool> DeleteService(string sqlDataSource, int targetID)
        {
            string query = @"
                            delete from dbo.Funcionario 
                            where id_funcionario = @id_funcionario";
            try
            {
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_funcionario", targetID);
                        dataReader = myCommand.ExecuteReader();

                        dataReader.Close();
                        databaseConnection.Close();
                    }
                }

                return true;
            }
            catch (SqlException ex)
            {
                Console.WriteLine("Erro na conexão com a base de dados: " + ex.Message);
                return false;
            }
            catch (ArgumentNullException ex)
            {
                Console.WriteLine("Erro de parametro inserido nulo: " + ex.Message);
                return false;
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
                return false;
            }
        }

        #endregion

        #region BACKLOG SERVICES

        /// <summary>
        /// Recuperação de uma palavra pass de um funcionario
        /// </summary>
        /// <param name="codigo">Codigo do funcionario</param>
        /// <param name="password">Nova palavra pass</param>
        /// <param name="sqlDataSource">String de conexão com a base de dados</param>
        /// <returns>Resultado de recuperação da palavra pass</returns>
        /// <exception cref="ArgumentException">Ocorre quando o funcionário do codigo inserido não existe</exception>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<bool> RecoverPasswordService(int codigo, string password, string sqlDataSource)
        {
            string query = @"update dbo.Funcionario 
                                            set pass_salt = @pass_salt, pass_hash = @pass_hash where codigo = @codigo";
            try
            {
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("codigo", codigo);

                        List<Funcionario> tempList = await GetAllService(sqlDataSource);
                        bool found = false;

                        for (int i = 0; i < tempList.Count() && found == false; i++)
                        {
                            if (tempList[i].codigo == codigo)
                                found = true;
                        }

                        if (found == false) throw new ArgumentException("Funcionário inexistente.", "codigo");

                        //Verificar que o user atual é quem tem o código associado

                        PasswordEncryption.CreatePasswordHash(password, out byte[] passwordHash, out byte[] passwordSalt);
                        string newHash = Convert.ToBase64String(passwordHash);
                        string newSalt = Convert.ToBase64String(passwordSalt);

                        myCommand.Parameters.AddWithValue("pass_hash", newHash);
                        myCommand.Parameters.AddWithValue("pass_salt", newSalt);

                        dataReader = myCommand.ExecuteReader();

                        dataReader.Close();
                        databaseConnection.Close();

                        return true;

                    }
                }
            }
            catch (SqlException ex)
            {
                Console.WriteLine("Erro na conexão com a base de dados: " + ex.Message);
                return false;
            }
            catch (ArgumentNullException ex)
            {
                Console.WriteLine("Erro de parametro inserido nulo: " + ex.Message);
                return false;
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
                return false;
            }
        }

        /// <summary>
        /// Login de um funcionário
        /// </summary>
        /// <param name="sqlDataSource">String de conexão com a base de dados</param>
        /// <param name="conta">Model de login de Funcionario</param>
        /// <param name="_configuration">Dependency Injection</param>
        /// <returns>True o login foi bem sucedido</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidOperationException">Ocorre quando o codigo do funcionario nao está atribuido</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<string> LoginService(string sqlDataSource, LoginFuncionario conta, IConfiguration _configuration)
        {
            string query = @"
                            select * from dbo.Funcionario 
                            where codigo = @codigo and estado != 'Inativo'";

            try
            {
                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("codigo", conta.codigo);
                        using (SqlDataReader reader = myCommand.ExecuteReader())
                        {
                            reader.Read();

                            Funcionario targetFuncionario = new Funcionario();
                            targetFuncionario.id_funcionario = reader.GetInt32(0);
                            targetFuncionario.id_ginasio = reader.GetInt32(1);
                            targetFuncionario.nome = reader.GetString(2);
                            targetFuncionario.is_admin = reader.GetBoolean(3);
                            targetFuncionario.codigo = reader.GetInt32(4);
                            targetFuncionario.pass_salt = reader.GetString(5);
                            targetFuncionario.pass_hash = reader.GetString(6);
                            targetFuncionario.estado = reader.GetString(7);

                            reader.Close();
                            databaseConnection.Close();

                            if (!PasswordEncryption.VerifyPasswordHash(conta.password, Convert.FromBase64String(targetFuncionario.pass_hash), Convert.FromBase64String(targetFuncionario.pass_salt)))
                            {
                                throw new ArgumentException("Password Errada.", "conta");
                            }

                            string token = Token.CreateTokenFuncionario(targetFuncionario, _configuration);

                            return token;
                        }
                    }
                }
            }
            catch (InvalidOperationException ex)
            {
                Console.WriteLine("Funcionário não existe\n" + ex.Message);
                return string.Empty;
            }
            catch (SqlException ex)
            {
                Console.WriteLine("Erro na conexão com a base de dados: " + ex.Message);
                return string.Empty;
            }
            catch (ArgumentNullException ex)
            {
                Console.WriteLine("Erro de parametro inserido nulo: " + ex.Message);
                return string.Empty;
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
                return string.Empty;
            }
        }

        /// <summary>
        /// Registo de um novo cliente no ginasio
        /// </summary>
        /// <param name="sqlDataSource">String de conexão com a base de dados</param>
        /// <param name="newCliente">Objeto que contem os dados do novo cliente</param>
        /// <returns>True se o cliente foi registado com sucesso</returns>
        /// /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<bool> RegistClienteService(string sqlDataSource, Cliente newCliente)
        {
            try
            {
                return await ClienteService.PostService(sqlDataSource, newCliente);
            }
            catch (SqlException ex)
            {
                Console.WriteLine("Erro na conexão com a base de dados: " + ex.Message);
                return false;
            }
            catch (InvalidCastException ex)
            {
                Console.WriteLine("Erro na conversão de dados: " + ex.Message);
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
            catch (Exception ex)
            {
                Console.Write(ex.ToString());
                return false;
            }
        }

        /// <summary>
        /// Remoção de um utilizador por parte do funcionário
        /// </summary>
        /// <param name="sqlDataSource">String de conexão com a base de dados</param>
        /// <param name="targetID">ID do cliente que se pretende arquivar/remover</param>
        /// <returns>Resultado da remoção de um cliente</returns>
        /// <exception cref="InvalidOperationException">Trata o caso em que ocorreu um erro de leitura dos dados</exception>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<bool> RemoveClienteService(string sqlDataSource, int targetID)
        {
            try
            {
                Cliente cliente = await ClienteService.GetByIDService(sqlDataSource, targetID);

                if (cliente == null) return false;

                cliente.estado = "Inativo";

                return await ClienteService.PatchService(sqlDataSource, cliente, targetID);
            }
            catch (InvalidOperationException ex)
            {
                Console.WriteLine("Cliente inexistente: " + ex.Message);

                return false;
            }
            catch (SqlException ex)
            {
                Console.WriteLine("Erro na conexão com a base de dados: " + ex.Message);
                return false;
            }
            catch (ArgumentNullException ex)
            {
                Console.WriteLine("Erro de parametro inserido nulo: " + ex.Message);
                return false;
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
                return false;
            }
        }

        /// <summary>
        /// Edição de um utilizador por parte do funcionário
        /// </summary>
        /// <param name="sqlDataSource">String de conexão com a base de dados</param>
        /// <param name="targetID">ID do cliente que se pretende editar</param>
        /// <param name="cliente">Objeto que contêm os dados do cliente atualizados</param>
        /// <returns>Resultado da edição de um cliente</returns>
        /// <exception cref="InvalidOperationException">Trata o caso em que ocorreu um erro de leitura dos dados</exception>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<bool> EditClienteService(string sqlDataSource, int targetID, Cliente cliente)
        {
            try
            {
                Cliente clienteVerify = await ClienteService.GetByIDService(sqlDataSource, targetID);

                if (clienteVerify == null) return false;

                return await ClienteService.PatchService(sqlDataSource, cliente, targetID);
            }
            catch (InvalidOperationException ex)
            {
                Console.WriteLine("Cliente inexistente: " + ex.Message);

                return false;
            }
            catch (SqlException ex)
            {
                Console.WriteLine("Erro na conexão com a base de dados: " + ex.Message);
                return false;
            }
            catch (ArgumentNullException ex)
            {
                Console.WriteLine("Erro de parametro inserido nulo: " + ex.Message);
                return false;
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
                return false;
            }
        }

        /// <summary>
        /// Alteração de stock de um produto
        /// </summary>
        /// <param name="sqlDataSource">String de conexão com a base de dados</param>
        /// <param name="quantidade">Novo valor de quantidade de stock na loja</param>
        /// <param name="targetID">ID do produto da Loja que se pretende alterar stock</param>
        /// <returns>Resultado da alteração de stock de um produto</returns>
        /// <exception cref="InvalidOperationException">Trata o caso em que ocorreu um erro de leitura dos dados</exception>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<bool> EditLojaStockService(string sqlDataSource, int quantidade, int targetID)
        {
            try
            {
                Loja produtoTarget = await LojaService.GetByIDService(sqlDataSource, targetID);

                if (produtoTarget == null) return false;

                produtoTarget.quantidade_produto = quantidade;

                return await LojaService.PatchService(sqlDataSource, produtoTarget, targetID);
            }
            catch (InvalidOperationException ex)
            {
                Console.WriteLine("Produto inexistente: " + ex.Message);

                return false;
            }
            catch (SqlException ex)
            {
                Console.WriteLine("Erro na conexão com a base de dados: " + ex.Message);
                return false;
            }
            catch (ArgumentNullException ex)
            {
                Console.WriteLine("Erro de parametro inserido nulo: " + ex.Message);
                return false;
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
                return false;
            }
        }

        /// <summary>
        /// Remoção de um funcionário com uso de verificações e remoção de dependencias
        /// </summary>
        /// <param name="sqlDataSource">String de conexão com a base de dados</param>
        /// <param name="targetID">ID do funcionario que se pretende arquivar/remover</param>
        /// <returns>Resultado da remoção de um funcionário</returns>
        /// <exception cref="InvalidOperationException">Trata o caso em que ocorreu um erro de leitura dos dados</exception>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<bool> DeleteFuncionarioService(string sqlDataSource, int targetID)
        {
            try
            {
                // Inativar todas as suas marcações
                List<Marcacao> marcacoesFuncionario = await MarcacaoService.GetAllByFuncionarioIDService(sqlDataSource, targetID);
                foreach (Marcacao marcacao in marcacoesFuncionario)
                {
                    if (marcacao.estado == "Ativo") marcacao.estado = "Inativo";
                    MarcacaoService.PatchService(sqlDataSource, marcacao, marcacao.id_marcacao);
                }

                // Remover o seu horario todo
                List<HorarioFuncionario> horarioFuncionario = await HorarioFuncionarioService.GetAllByFuncionarioIDService(sqlDataSource, targetID);
                foreach (HorarioFuncionario dia in horarioFuncionario)
                {
                    HorarioFuncionarioService.DeleteService(sqlDataSource, dia.id_funcionario_horario);
                }

                // Inativar funcionário
                Funcionario funcionario = await GetByIDService(sqlDataSource, targetID);

                if (funcionario == null || funcionario.is_admin == true) return false;

                funcionario.estado = "Inativo";

                return await PatchService(sqlDataSource, funcionario, targetID);
            }
            catch (InvalidOperationException ex)
            {
                Console.WriteLine("Funcionario inexistente: " + ex.Message);

                return false;
            }
            catch (SqlException ex)
            {
                Console.WriteLine("Erro na conexão com a base de dados: " + ex.Message);
                return false;
            }
            catch (ArgumentNullException ex)
            {
                Console.WriteLine("Erro de parametro inserido nulo: " + ex.Message);
                return false;
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
                return false;
            }
        }

        /// <summary>
        /// Edição de um produto na loja
        /// </summary>
        /// <param name="sqlDataSource">String de conexão à base de dados</param>
        /// <param name="produto">Objeto que contém dados atualizados do produto da Loja</param>
        /// <param name="targetID">ID do produto pretendido a mudar</param>
        /// <returns>Resultado da edição de um produto</returns>
        /// <exception cref="InvalidOperationException">Trata o caso em que ocorreu um erro de leitura dos dados</exception>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<bool> EditLojaService(string sqlDataSource, Loja produto, int targetID)
        {
            try
            {
                Loja produtoVerify = await LojaService.GetByIDService(sqlDataSource, targetID);

                if (produtoVerify == null) return false;

                return await LojaService.PatchService(sqlDataSource, produto, targetID);
            }
            catch (InvalidOperationException ex)
            {
                Console.WriteLine("Produto inexistente: " + ex.Message);

                return false;
            }
            catch (SqlException ex)
            {
                Console.WriteLine("Erro na conexão com a base de dados: " + ex.Message);
                return false;
            }
            catch (ArgumentNullException ex)
            {
                Console.WriteLine("Erro de parametro inserido nulo: " + ex.Message);
                return false;
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
                return false;
            }
        }

        /// <summary>
        /// Alteração do valor de lotacao atual no ginasio
        /// </summary>
        /// <param name="sqlDataSource">String de conexão à base de dados</param>
        /// <param name="targetID">ID do ginásio a alterar a sua ocupação</param>
        /// <param name="lotacao">Valor da lotação atual do ginásio</param>
        /// <returns>Resultado da alteracao da lotacao atual do ginasio</returns>
        /// <exception cref="InvalidOperationException">Trata o caso em que ocorreu um erro de leitura dos dados</exception>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<bool> EditLotacaoGymLogicService(string sqlDataSource, int targetID, int lotacao)
        {
            try
            {
                Ginasio ginasioVerify = await GinasioService.GetByIDService(sqlDataSource, targetID);

                if (ginasioVerify == null) return false;

                ginasioVerify.lotacao = lotacao;

                return await GinasioService.PatchService(sqlDataSource, ginasioVerify, targetID);
            }
            catch (InvalidOperationException ex)
            {
                Console.WriteLine("Ginasio inexistente: " + ex.Message);

                return false;
            }
            catch (SqlException ex)
            {
                Console.WriteLine("Erro na conexão com a base de dados: " + ex.Message);
                return false;
            }
            catch (ArgumentNullException ex)
            {
                Console.WriteLine("Erro de parametro inserido nulo: " + ex.Message);
                return false;
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
                return false;
            }
        }

        /// <summary>
        /// Ver avaliaçoes de um ginasio
        /// </summary>
        /// <param name="sqlDataSource">String de conexão à base de dados</param>
        /// <param name="codigofuncionario">Codigo do funcionario que faz o request</param>
        /// <returns></returns>
        public static async Task<List<Classificacao>> GetAvaliacoesOnGymService(string sqlDataSource, int codigofuncionario)
        {
            try
            {
                string query = @"
                            select * from dbo.Funcionario 
                            where codigo = @codigo and estado != 'Inativo'";


                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("codigo", codigofuncionario);
                        using (SqlDataReader reader = myCommand.ExecuteReader())
                        {
                            reader.Read();

                            int id_ginasio = reader.GetInt32(1);

                            List<Classificacao> list = await ClassificacaoService.GetAllClassificationsByGinasioIDService(sqlDataSource, id_ginasio);

                            reader.Close();
                            databaseConnection.Close();

                            return list;
                        }
                    }
                }
            }
            catch (SqlException ex)
            {
                Console.WriteLine("Erro na conexão com a base de dados: " + ex.Message);
                return null;
            }
            catch (ArgumentNullException ex)
            {
                Console.WriteLine("Erro de parametro inserido nulo: " + ex.Message);
                return null;
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
                return null;
            }
        }

        #endregion
    }
}
