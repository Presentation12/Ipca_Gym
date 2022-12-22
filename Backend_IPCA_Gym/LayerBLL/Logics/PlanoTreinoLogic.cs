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
    /// Classe que contém a lógica do response da entidade PlanoTreino
    /// </summary>
    public class PlanoTreinoLogic
    {
        #region DEFAULT REQUESTS

        /// <summary>
        /// Método que recebe os dados do serviço de obter todos os planos de treino
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> GetAllLogic(string sqlDataSource)
        {
            Response response = new Response();
            List<PlanoTreino> planoTreinoList = await PlanoTreinoService.GetAllService(sqlDataSource);

            if (planoTreinoList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Lista de planos de treino obtida com sucesso";
                response.Data = new JsonResult(planoTreinoList);
            }

            return response;
        }

        /// <summary>
        /// Método que recebe os dados do serviço de obter um plano de treino em específico
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="targetID">ID do PlanoTreino que é pretendido retornar</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> GetByIDLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            PlanoTreino ptreino = await PlanoTreinoService.GetByIDService(sqlDataSource, targetID);

            if (ptreino != null)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Plano de treino obtido com sucesso!";
                response.Data = new JsonResult(ptreino);
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de criar um plano de treino
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="newPlan">Objeto com os dados do PlanoTreino a ser criado</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> PostLogic(string sqlDataSource, PlanoTreino newPlano)
        {
            Response response = new Response();
            bool creationResult = await PlanoTreinoService.PostService(sqlDataSource, newPlano);

            if (creationResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Plano de treino criado com sucesso!");
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de atualizar um plano de treino
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="plan">Objeto que contém os dados atualizados do PlanoTreino</param>
        /// <param name="targetID">ID do PlanoTreino que é pretendido atualizar</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> PatchLogic(string sqlDataSource, PlanoTreino plano, int targetID)
        {
            Response response = new Response();
            bool updateResult = await PlanoTreinoService.PatchService(sqlDataSource, plano, targetID);

            if (updateResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Plano de treino alterado com sucesso!");
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de eliminar um plano de treino
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="targetID">ID do PlanoTreino que é pretendido eliminar</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> DeleteLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            bool deleteResult = await PlanoTreinoService.DeleteService(sqlDataSource, targetID);

            if (deleteResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Plano de treino apagado com sucesso!");
            }

            return response;
        }

        #endregion

        #region BACKLOG REQUESTS

        /// <summary>
        /// Método que recebe os dados do serviço de obter todos os planos de treino de um ginásio
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="targetID">ID do ginásio que é pretendido retornar os planos de treino</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> GetAllByGinasioIDLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            List<PlanoTreino> planoTreinoList = await PlanoTreinoService.GetAllByGinasioIDService(sqlDataSource, targetID);

            if (planoTreinoList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Lista de planos de treino obtida com sucesso";
                response.Data = new JsonResult(planoTreinoList);
            }

            return response;
        }

        #endregion
    }
}
