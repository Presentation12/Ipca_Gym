using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Threading.Tasks;
using System.Web;
using System.Web.Services;

namespace WebServiceIPCAGym.Services
{
    /// <summary>
    /// Serviço para efetuar o uma busca de todos os clientes no IPCAGym
    /// </summary>
    [WebService(Namespace = "www.ipca.pt",
                Description = "Web service exemplo para a funcionalidade de obter todos os Clientes [GET] do IPCAGym",
                Name = "IPCAGym")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    public class ClientesWS : WebService
    {
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
        public List<Cliente> GetAllService()
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
    }
}
