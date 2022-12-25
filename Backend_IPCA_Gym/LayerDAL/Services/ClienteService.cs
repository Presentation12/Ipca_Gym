using LayerBOL.Models;
using System.Data;
using System.Data.SqlClient;
using System.Net.Mail;
using System.Net;
using Microsoft.Extensions.Configuration;

namespace LayerDAL.Services
{
    public class ClienteService
    {
        #region DEFAULT REQUESTS

        /// <summary>
        /// Leitura dos dados de todos os clientes da base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <returns>Lista de clientes se uma leitura bem sucedida, null em caso de erro</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="InvalidOperationException">Trata o caso em que ocorreu um erro de leitura dos dados</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="IndexOutOfRangeException">Trata o caso em que o índice da coluna da base de dados acessado é inválido</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<List<Cliente>> GetAllService(string sqlDataSource)
        {
            string query = @"select * from dbo.Cliente";
            List<Cliente> clientes = new List<Cliente>();

            try
            {
                SqlDataReader dataReader;
                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
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
                                cliente.id_plano_nutricional = null;
                            }
                            cliente.nome = dataReader["nome"].ToString();
                            cliente.mail = dataReader["mail"].ToString();
                            cliente.telemovel = Convert.ToInt32(dataReader["telemovel"]);
                            cliente.pass_salt = dataReader["pass_salt"].ToString();
                            cliente.pass_hash = dataReader["pass_hash"].ToString();
                            if (!Convert.IsDBNull(dataReader["peso"]))
                            {
                                cliente.peso = Convert.ToDouble(dataReader["peso"]);
                            }
                            else
                            {
                                cliente.peso = null;
                            }
                            if (!Convert.IsDBNull(dataReader["altura"]))
                            {
                                cliente.altura = Convert.ToInt32(dataReader["altura"]);
                            }
                            else
                            {
                                cliente.altura = null;
                            }
                            if (!Convert.IsDBNull(dataReader["gordura"]))
                            {
                                cliente.gordura = Convert.ToDouble(dataReader["gordura"]);
                            }
                            else
                            {
                                cliente.gordura = null;
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
        /// Leitura dos dados de um cliente através do seu id na base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <param name="targetID">ID do cliente a ser lido</param>
        /// <returns>Cliente se uma leitura bem sucedida, ou null em caso de erro</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="InvalidOperationException">Trata o caso em que ocorreu um erro de leitura dos dados</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="IndexOutOfRangeException">Trata o caso em que o índice da coluna da base de dados acessado é inválido</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<Cliente> GetByIDService(string sqlDataSource, int targetID)
        {
            string query = @"select * from dbo.Cliente where id_cliente = @id_cliente";
            try
            {
                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_cliente", targetID);

                        using (SqlDataReader reader = myCommand.ExecuteReader())
                        {
                            reader.Read();

                            Cliente targetCliente = new Cliente();
                            targetCliente.id_cliente = reader.GetInt32(0);
                            targetCliente.id_ginasio = reader.GetInt32(1);
                            if (!Convert.IsDBNull(reader["id_plano_nutricional"]))
                            {
                                targetCliente.id_plano_nutricional = reader.GetInt32(2);
                            }
                            else
                            {
                                targetCliente.id_plano_nutricional = null;
                            }
                            targetCliente.nome = reader.GetString(3);
                            targetCliente.mail = reader.GetString(4);
                            targetCliente.telemovel = reader.GetInt32(5);
                            targetCliente.pass_salt = reader.GetString(6);
                            targetCliente.pass_hash = reader.GetString(7);

                            if (!Convert.IsDBNull(reader["peso"]))
                            {
                                targetCliente.peso = reader.GetDouble(8);
                            }
                            else
                            {
                                targetCliente.peso = null;
                            }

                            if (!Convert.IsDBNull(reader["altura"]))
                            {
                                targetCliente.altura = reader.GetInt32(9);
                            }
                            else
                            {
                                targetCliente.altura = null;
                            }

                            if (!Convert.IsDBNull(reader["gordura"]))
                            {
                                targetCliente.gordura = reader.GetDouble(10);
                            }
                            else
                            {
                                targetCliente.gordura = null;
                            }

                            if (!Convert.IsDBNull(reader["foto_perfil"]))
                            {
                                targetCliente.foto_perfil = reader.GetString(11);
                            }
                            else
                            {
                                targetCliente.foto_perfil = null;
                            }
                            targetCliente.estado = reader.GetString(12);

                            reader.Close();
                            databaseConnection.Close();

                            return targetCliente;
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
        /// Inserção dos dados de um novo cliente na base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <param name="newCliente">Objeto com os dados do novo cliente</param>
        /// <returns>True se a escrita dos dados foi bem sucedida, false em caso de erro.</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<bool> PostService(string sqlDataSource, Cliente newCliente)
        {
            string query = @"
                            insert into dbo.Cliente (id_ginasio, id_plano_nutricional, nome, mail, telemovel, pass_salt, pass_hash, peso, altura, gordura, foto_perfil, estado)
                            values (@id_ginasio, @id_plano_nutricional, @nome, @mail, @telemovel, @pass_salt, @pass_hash, @peso, @altura, @gordura, @foto_perfil, @estado)";

            try
            {
                SqlDataReader dataReader;
                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_ginasio", newCliente.id_ginasio);

                        if (newCliente.id_plano_nutricional != null) myCommand.Parameters.AddWithValue("id_plano_nutricional", newCliente.id_plano_nutricional);
                        else myCommand.Parameters.AddWithValue("id_plano_nutricional", DBNull.Value);

                        myCommand.Parameters.AddWithValue("nome", newCliente.nome);
                        myCommand.Parameters.AddWithValue("mail", newCliente.mail);
                        myCommand.Parameters.AddWithValue("telemovel", newCliente.telemovel);

                        PasswordEncryption.CreatePasswordHash(newCliente.pass_salt, out byte[] passwordHash, out byte[] passwordSalt);
                        newCliente.pass_hash = Convert.ToBase64String(passwordHash);
                        newCliente.pass_salt = Convert.ToBase64String(passwordSalt);

                        myCommand.Parameters.AddWithValue("pass_hash", newCliente.pass_hash);
                        myCommand.Parameters.AddWithValue("pass_salt", newCliente.pass_salt);

                        if (newCliente.peso != null) myCommand.Parameters.AddWithValue("peso", newCliente.peso);
                        else myCommand.Parameters.AddWithValue("peso", DBNull.Value);

                        if (newCliente.altura != null) myCommand.Parameters.AddWithValue("altura", newCliente.altura);
                        else myCommand.Parameters.AddWithValue("altura", DBNull.Value);

                        if (newCliente.gordura != null) myCommand.Parameters.AddWithValue("gordura", newCliente.gordura);
                        else myCommand.Parameters.AddWithValue("gordura", DBNull.Value);

                        if (!string.IsNullOrEmpty(newCliente.foto_perfil)) myCommand.Parameters.AddWithValue("foto_perfil", newCliente.foto_perfil);
                        else myCommand.Parameters.AddWithValue("foto_perfil", DBNull.Value);

                        myCommand.Parameters.AddWithValue("estado", newCliente.estado);
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
        /// Atualiza os dados de um cliente através do seu id na base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <param name="cliente">Objeto com os novos dados da cliente</param>
        /// <param name="targetID">ID do cliente a ser atualizado</param>
        /// <returns>True se a atualização dos dados foi bem-sucedida, false caso contrário</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<bool> PatchService(string sqlDataSource, Cliente cliente, int targetID)
        {
            string query = @"
                            update dbo.Cliente 
                            set id_ginasio = @id_ginasio, 
                            id_plano_nutricional = @id_plano_nutricional, 
                            nome = @nome,
                            mail = @mail,
                            telemovel = @telemovel,
                            pass_salt = @pass_salt,
                            pass_hash = @pass_hash,
                            peso = @peso,
                            altura = @altura,
                            gordura = @gordura,
                            foto_perfil = @foto_perfil,
                            estado = @estado
                            where id_cliente = @id_cliente";
            try
            {
                Cliente clienteAtual = await GetByIDService(sqlDataSource, targetID);
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_cliente", cliente.id_cliente != 0 ? cliente.id_cliente : clienteAtual.id_cliente);
                        myCommand.Parameters.AddWithValue("id_ginasio", cliente.id_ginasio != 0 ? cliente.id_ginasio : clienteAtual.id_ginasio);
                        if(clienteAtual.id_plano_nutricional != null && cliente.id_plano_nutricional != null)
                            myCommand.Parameters.AddWithValue("id_plano_nutricional", cliente.id_plano_nutricional == null ? cliente.id_plano_nutricional : clienteAtual.id_plano_nutricional);
                        else
                            myCommand.Parameters.AddWithValue("id_plano_nutricional", DBNull.Value);

                        myCommand.Parameters.AddWithValue("nome", !string.IsNullOrEmpty(cliente.nome) ? cliente.nome : clienteAtual.nome);
                        myCommand.Parameters.AddWithValue("mail", !string.IsNullOrEmpty(cliente.mail) ? cliente.mail : clienteAtual.mail);
                        myCommand.Parameters.AddWithValue("telemovel", cliente.telemovel != 0 ? cliente.telemovel : clienteAtual.telemovel);
                        myCommand.Parameters.AddWithValue("pass_hash", clienteAtual.pass_hash);
                        myCommand.Parameters.AddWithValue("pass_salt", clienteAtual.pass_salt);

                        if (clienteAtual.peso != null && cliente.peso != null)
                            myCommand.Parameters.AddWithValue("peso", cliente.peso == null ? cliente.peso : clienteAtual.peso);
                        else
                            myCommand.Parameters.AddWithValue("peso", DBNull.Value);
                        
                        if (clienteAtual.altura != null && cliente.altura != null)
                            myCommand.Parameters.AddWithValue("altura", cliente.altura == null ? cliente.altura : clienteAtual.altura);
                        else
                            myCommand.Parameters.AddWithValue("altura", DBNull.Value);

                        if (clienteAtual.gordura != null && cliente.gordura != null)
                            myCommand.Parameters.AddWithValue("gordura", cliente.gordura == null ? cliente.gordura : clienteAtual.gordura);
                        else
                            myCommand.Parameters.AddWithValue("gordura", DBNull.Value);

                        if (clienteAtual.foto_perfil != null && cliente.foto_perfil != null)
                            myCommand.Parameters.AddWithValue("foto_perfil", cliente.foto_perfil == null ? cliente.foto_perfil : clienteAtual.foto_perfil);
                        else
                            myCommand.Parameters.AddWithValue("foto_perfil", DBNull.Value);

                        myCommand.Parameters.AddWithValue("estado", !string.IsNullOrEmpty(cliente.estado) ? cliente.estado : clienteAtual.estado);

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
        /// Remoção de um cliente da base de dados pelo seu ID
        /// </summary>
        /// <param name="sqlDataSource">String de conexão com a base de dados</param>
        /// <param name="targetID">ID do cliente a ser removido</param>
        /// <returns>True se a remoção foi bem sucedida, false em caso de erro</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<bool> DeleteService(string sqlDataSource, int targetID)
        {
            string query = @"
                            delete from dbo.Cliente 
                            where id_cliente = @id_cliente";

            try
            {
                SqlDataReader dataReader;
                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_cliente", targetID);
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

        #region BACKLOG REQUESTS

        /// <summary>
        /// Recuperação de uma palavra pass de um cliente
        /// </summary>
        /// <param name="codigo">Mail do cliente</param>
        /// <param name="password">Nova palavra pass</param>
        /// <param name="sqlDataSource">String de conexão com a base de dados</param>
        /// <returns>Resultado de recuperação da palavra pass</returns>
        /// <exception cref="ArgumentException">Ocorre quando o funcionário do codigo inserido não existe</exception>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<bool> RecoverPasswordService(string mail, string password, string sqlDataSource)
        {
            string query = @"update dbo.Cliente set pass_salt = @pass_salt, pass_hash = @pass_hash where mail = @mail";
            try
            {
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("mail", mail);

                        List<Cliente> tempList = await GetAllService(sqlDataSource);
                        bool found = false;

                        for (int i = 0; i < tempList.Count() && found == false; i++)
                        {
                            if (tempList[i].mail == mail)
                                found = true;
                        }

                        if (found == false) throw new ArgumentException("Cliente inexistente.", "mail");

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
        /// Login de um cliente
        /// </summary>
        /// <param name="sqlDataSource">String de conexão com a base de dados</param>
        /// <param name="conta">Model de login de Cliente</param>
        /// <param name="_configuration">Dependency Injection</param>
        /// <returns>True o login foi bem sucedido</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidOperationException">Ocorre quando o codigo do funcionario nao está atribuido</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<string> LoginService(string sqlDataSource, LoginCliente conta, IConfiguration _configuration)
        {
            string query = @"
                            select * from dbo.Cliente 
                            where mail = @mail";

            try
            {
                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("mail", conta.mail);
                        using (SqlDataReader reader = myCommand.ExecuteReader())
                        {
                            reader.Read();

                            Cliente targetCliente = new Cliente();
                            targetCliente.id_cliente = reader.GetInt32(0);
                            targetCliente.id_ginasio = reader.GetInt32(1);

                            if (!Convert.IsDBNull(reader["id_plano_nutricional"]))
                                targetCliente.id_plano_nutricional = reader.GetInt32(2);
                            else
                                targetCliente.id_plano_nutricional = null;

                            targetCliente.nome = reader.GetString(3);
                            targetCliente.mail = reader.GetString(4);
                            targetCliente.telemovel = reader.GetInt32(5);
                            targetCliente.pass_salt = reader.GetString(6);
                            targetCliente.pass_hash = reader.GetString(7);

                            if (!Convert.IsDBNull(reader["peso"]))
                                targetCliente.peso = reader.GetDouble(8);
                            else
                                targetCliente.peso = null;

                            if (!Convert.IsDBNull(reader["altura"]))
                                targetCliente.altura = reader.GetInt32(9);
                            else
                                targetCliente.altura = null;

                            if (!Convert.IsDBNull(reader["gordura"]))
                                targetCliente.gordura = reader.GetDouble(10);
                            else
                                targetCliente.gordura = null;

                            if (!Convert.IsDBNull(reader["foto_perfil"]))
                                targetCliente.foto_perfil = reader.GetString(11);
                            else
                                targetCliente.foto_perfil = null;

                            targetCliente.estado = reader.GetString(12);

                            reader.Close();
                            databaseConnection.Close();

                            if (!PasswordEncryption.VerifyPasswordHash(conta.password, Convert.FromBase64String(targetCliente.pass_hash), Convert.FromBase64String(targetCliente.pass_salt)))
                            {
                                throw new ArgumentException("Password Errada.", "conta");
                            }

                            string token = Token.CreateTokenCliente(targetCliente, _configuration);

                            return token;
                        }
                    }
                }
            }
            catch (InvalidOperationException ex)
            {
                Console.WriteLine("Cliente não existe\n" + ex.Message);
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

        #endregion
    }
}
