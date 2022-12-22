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
    /// Classe que contém a lógica do response da entidade Refeicao
    /// </summary>
    public class RefeicaoLogic
    {
        /// <summary>
        /// Método que recebe os dados do serviço de obter todas as refeicoes
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> GetAllLogic(string sqlDataSource)
        {
            Response response = new Response();
            List<Refeicao> refeicaoList = await RefeicaoService.GetAllService(sqlDataSource);

            if (refeicaoList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Lista de refeicoes obtida com sucesso";
                response.Data = new JsonResult(refeicaoList);
            }

            return response;
        }

        /// <summary>
        /// Método que recebe os dados do serviço de obter uma refeicao em específico
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="targetID">ID da Refeicao que é pretendida retornar</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> GetByIDLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            Refeicao refeicao = await RefeicaoService.GetByIDService(sqlDataSource, targetID);

            if (refeicao != null)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Refeicao obtida com sucesso!";
                response.Data = new JsonResult(refeicao);
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de criar uma refeicao
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="newRefeicao">Objeto com os dados da Refeicao a ser criada</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> PostLogic(string sqlDataSource, Refeicao newRefeicao)
        {
            Response response = new Response();
            bool creationResult = await RefeicaoService.PostService(sqlDataSource, newRefeicao);

            if (creationResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Refeicao criada com sucesso!");
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de atualizar uma refeicao
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="refeicao">Objeto que contém os dados atualizados da Refeicao</param>
        /// <param name="targetID">ID da Refeicao que é pretendida atualizar</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> PatchLogic(string sqlDataSource, Refeicao refeicao, int targetID)
        {
            Response response = new Response();
            bool updateResult = await RefeicaoService.PatchService(sqlDataSource, refeicao, targetID);

            if (updateResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Refeicao alterada com sucesso!");
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de eliminar uma refeicao
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="targetID">ID da Refeicao que é pretendida eliminar</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> DeleteLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            bool deleteResult = await RefeicaoService.DeleteService(sqlDataSource, targetID);

            if (deleteResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Refeicao apagada com sucesso!");
            }

            return response;
        }
    }
}