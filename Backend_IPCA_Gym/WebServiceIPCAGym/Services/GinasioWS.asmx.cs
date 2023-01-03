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
    /// Serviço para efetuar uma alteração num ginásio no IPCAGym
    /// </summary>
    [WebService(Namespace = "www.ipca.pt",
                Description = "Web service exemplo para a funcionalidade de editar um ginásio [PATCH] do IPCAGym",
                Name = "IPCAGym")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    public class GinasioWS : WebService
    {

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
        public bool PatchService(int targetID, string instituicao, int contacto, string foto, string estado, int lotacao, int lotacaoMax)
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
    }
}
