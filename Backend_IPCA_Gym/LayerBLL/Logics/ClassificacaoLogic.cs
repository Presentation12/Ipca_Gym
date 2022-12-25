using LayerBLL.Utils;
using LayerBOL.Models;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Numerics;
using System.Text;
using System.Threading.Tasks;
using LayerDAL.Services;
using System.Data.SqlClient;

namespace LayerBLL.Logics
{
    /// <summary>
    /// Classe que contém a lógica do response da entidade Classificacao
    /// </summary>
    public class ClassificacaoLogic
    {
        #region DEFAULT REQUESTS
        /// <summary>
        /// Método que recebe os dados do serviço de obter todas as classificacoes
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="InvalidOperationException">Trata o caso em que ocorreu um erro de leitura dos dados</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="IndexOutOfRangeException">Trata o caso em que o índice da coluna da base de dados acessado é inválido</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<Response> GetAllLogic(string sqlDataSource)
        {
            Response response = new Response();
            
            try
            {
                List<Classificacao> classificacaoList = await ClassificacaoService.GetAllService(sqlDataSource);

                if (classificacaoList.Count != 0)
                {
                    response.StatusCode = StatusCodes.SUCCESS;
                    response.Message = "Lista de classificacoes obtida com sucesso";
                    response.Data = new JsonResult(classificacaoList);
                }

                return response;
            }
            catch (SqlException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro na conexão com a base de dados: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (InvalidCastException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro na conversão de dados: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (InvalidOperationException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro de leitura dos dados: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (FormatException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro de tipo de dados: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (IndexOutOfRangeException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro de acesso a uma coluna da base de dados: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (Exception ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
        }

        /// <summary>
        /// Método que recebe os dados do serviço de obter uma classificacao em específico
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="targetID">ID da Classificacao que é pretendida retornar</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="InvalidOperationException">Trata o caso em que ocorreu um erro de leitura dos dados</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="IndexOutOfRangeException">Trata o caso em que o índice da coluna da base de dados acessado é inválido</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<Response> GetByIDLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            
            try
            {
                Classificacao classificacao = await ClassificacaoService.GetByIDService(sqlDataSource, targetID);

                if (classificacao != null)
                {
                    response.StatusCode = StatusCodes.SUCCESS;
                    response.Message = "Classificacao obtida com sucesso";
                    response.Data = new JsonResult(classificacao);
                }

                return response;
            }
            catch (SqlException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro na conexão com a base de dados: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (InvalidCastException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro na conversão de dados: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (InvalidOperationException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro de leitura dos dados: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (FormatException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro de tipo de dados: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (IndexOutOfRangeException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro de acesso a uma coluna da base de dados: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (ArgumentNullException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro de parametro inserido nulo: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (Exception ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de criar uma classificacao
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="newClassificacao">Objeto com os dados da Classificacao a ser criada</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<Response> PostLogic(string sqlDataSource, Classificacao newClassificacao)
        {
            Response response = new Response();
            
            try
            {
                bool creationResult = await ClassificacaoService.PostService(sqlDataSource, newClassificacao);

                if (creationResult)
                {
                    response.StatusCode = StatusCodes.SUCCESS;
                    response.Message = "Success!";
                    response.Data = new JsonResult("Classificacao adicionada com sucesso");
                }

                return response;
            }
            catch (SqlException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro na conexão com a base de dados: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (InvalidCastException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro na conversão de dados: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (FormatException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro de tipo de dados: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (ArgumentNullException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro de parametro inserido nulo: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (Exception ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de atualizar uma classificacao
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="classificacao">Objeto que contém os dados atualizados da Classificacao</param>
        /// <param name="targetID">ID da Classificacao que é pretendida atualizar</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<Response> PatchLogic(string sqlDataSource, Classificacao classificacao, int targetID)
        {
            Response response = new Response();
            
            try
            {
                bool updateResult = await ClassificacaoService.PatchService(sqlDataSource, classificacao, targetID);

                if (updateResult)
                {
                    response.StatusCode = StatusCodes.SUCCESS;
                    response.Message = "Success!";
                    response.Data = new JsonResult("Classificacao alterada com sucesso");
                }

                return response;
            }
            catch (SqlException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro na conexão com a base de dados: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (InvalidCastException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro na conversão de dados: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (FormatException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro de tipo de dados: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (ArgumentNullException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro de parametro inserido nulo: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (Exception ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de eliminar uma classificacao
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="targetID">ID da Classificacao que é pretendida eliminar</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<Response> DeleteLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            
            try
            {
                bool deleteResult = await ClassificacaoService.DeleteService(sqlDataSource, targetID);

                if (deleteResult)
                {
                    response.StatusCode = StatusCodes.SUCCESS;
                    response.Message = "Success!";
                    response.Data = new JsonResult("Classificacao removida com sucesso");
                }

                return response;
            }
            catch (SqlException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro na conexão com a base de dados: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (ArgumentNullException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro de parametro inserido nulo: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (Exception ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
        }
        #endregion

        #region BACKLOG REQUESTS

        /// <summary>
        /// Método que recebe os dados do serviço de obter todas as classificacoes de um ginásio
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="targetID">ID do ginásio que é pretendido retornar as classificacoes</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="InvalidOperationException">Trata o caso em que ocorreu um erro de leitura dos dados</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="IndexOutOfRangeException">Trata o caso em que o índice da coluna da base de dados acessado é inválido</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<Response> GetAllClassificationsByGinasioIDLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            
            try
            {
                List<Classificacao> classificacaoList = await ClassificacaoService.GetAllClassificationsByGinasioIDService(sqlDataSource, targetID);

                if (classificacaoList.Count != 0)
                {
                    response.StatusCode = StatusCodes.SUCCESS;
                    response.Message = "Lista de classificacoes obtida com sucesso";
                    response.Data = new JsonResult(classificacaoList);
                }

                return response;
            }
            catch (SqlException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro na conexão com a base de dados: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (InvalidCastException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro na conversão de dados: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (InvalidOperationException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro de leitura dos dados: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (FormatException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro de tipo de dados: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (IndexOutOfRangeException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro de acesso a uma coluna da base de dados: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (ArgumentNullException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro de parametro inserido nulo: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (Exception ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
        }

        #endregion
    }
}
