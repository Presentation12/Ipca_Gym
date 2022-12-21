using LayerBOL.Models;
using System.Data;
using System.Data.SqlClient;

namespace LayerDAL.Services
{
    public class FuncionarioService
    {
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
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
                return null;
            }
        }

        public static async Task<Funcionario> GetByIDService(string sqlDataSource, int targetID)
        {
            string query = @"select * from dbo.Funcionario where id_funcionario = @id_funcionario";

            try
            {
                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        Console.WriteLine(targetID);
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
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
                return null;
            }
        }

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
                        myCommand.Parameters.AddWithValue("pass_salt", newFuncionario.pass_salt);
                        myCommand.Parameters.AddWithValue("pass_hash", newFuncionario.pass_hash);
                        myCommand.Parameters.AddWithValue("estado", newFuncionario.estado);

                        dataReader = myCommand.ExecuteReader();

                        dataReader.Close();
                        databaseConnection.Close();
                    }
                }

                return true;
            }
            catch (Exception ex)
            {
                Console.Write(ex.ToString());
                return false;
            }
        }

        public static async Task<bool> PatchService(string sqlDataSource, Funcionario funcionario, int targetID)
        {
            string query = @"
                            update dbo.Funcionario 
                            set id_ginasio = @id_ginasio, 
                            nome = @nome, 
                            is_admin = @is_admin,
                            codigo = @codigo,
                            pass_salt = @pass_salt,
                            pass_hash = @pass_hash,
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
                        myCommand.Parameters.AddWithValue("pass_salt", !string.IsNullOrEmpty(funcionario.pass_salt) ? funcionario.pass_salt : funcionarioAtual.pass_salt);
                        myCommand.Parameters.AddWithValue("pass_hash", !string.IsNullOrEmpty(funcionario.pass_hash) ? funcionario.pass_hash : funcionarioAtual.pass_hash);
                        myCommand.Parameters.AddWithValue("estado", !string.IsNullOrEmpty(funcionario.estado) ? funcionario.estado : funcionarioAtual.estado);

                        dataReader = myCommand.ExecuteReader();

                        dataReader.Close();
                        databaseConnection.Close();
                    }
                }

                return true;
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
                return false;
            }
        }

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
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
                return false;
            }
        }
    }
}
