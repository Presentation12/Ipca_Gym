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

namespace LayerBLL.Logics
{
    /// <summary>
    /// Classe que contém a lógica do response da entidade Classificacao
    /// </summary>
    public class ClassificacaoLogic
    {
        /// <summary>
        /// Método que recebe os dados do serviço de obter todas as classificacoes
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> GetAllLogic(string sqlDataSource)
        {
            Response response = new Response();
            List<Classificacao> classificacaoList = await ClassificacaoService.GetAllService(sqlDataSource);

            if (classificacaoList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Lista de classificacoes obtida com sucesso";
                response.Data = new JsonResult(classificacaoList);
            }

            return response;
        }

        /// <summary>
        /// Método que recebe os dados do serviço de obter uma classificacao em específico
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="targetID">ID da Classificacao que é pretendida retornar</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> GetByIDLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            Classificacao classificacao = await ClassificacaoService.GetByIDService(sqlDataSource, targetID);

            if (classificacao != null)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Classificacao obtida com sucesso";
                response.Data = new JsonResult(classificacao);
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de criar uma classificacao
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="newClassificacao">Objeto com os dados da Classificacao a ser criada</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> PostLogic(string sqlDataSource, Classificacao newClassificacao)
        {
            Response response = new Response();
            bool creationResult = await ClassificacaoService.PostService(sqlDataSource, newClassificacao);

            if (creationResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Classificacao adicionada com sucesso");
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de atualizar uma classificacao
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="classificacao">Objeto que contém os dados atualizados da Classificacao</param>
        /// <param name="targetID">ID da Classificacao que é pretendida atualizar</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> PatchLogic(string sqlDataSource, Classificacao classificacao, int targetID)
        {
            Response response = new Response();
            bool updateResult = await ClassificacaoService.PatchService(sqlDataSource, classificacao, targetID);

            if (updateResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Classificacao alterada com sucesso");
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de eliminar uma classificacao
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="targetID">ID da Classificacao que é pretendida eliminar</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> DeleteLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            bool deleteResult = await ClassificacaoService.DeleteService(sqlDataSource, targetID);

            if (deleteResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Classificacao removida com sucesso");
            }

            return response;
        }
    }
}
