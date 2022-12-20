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
    public class PlanoTreinoLogic
    {
        public static async Task<Response> GetAllLogic(string sqlDataSource)
        {
            Response response = new Response();
            List<PlanoTreino> planotreinoList = await PlanoTreinoService.GetAllService(sqlDataSource);

            if (planotreinoList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Lista de planos de treino obtida com sucesso";
                response.Data = new JsonResult(planotreinoList);
            }

            return response;
        }

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
    }
}
